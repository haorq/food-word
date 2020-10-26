package com.meiyuan.catering.admin.service.marketing;

import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventAddDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventEditDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventPageQueryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventClient;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventDetailVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventPageQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 简单描述
 **/

@Slf4j
@Service
public class AdminMarketingSeckillEventService {

    @Autowired
    private MarketingSeckillEventClient seckillEventClient;

    public Result<PageData<MarketingSeckillEventPageQueryVO>> pageQuery(MarketingSeckillEventPageQueryDTO dto) {
        Result<PageData<MarketingSeckillEventPageQueryVO>> dataResult = seckillEventClient.pageQuery(dto);
        List<MarketingSeckillEventPageQueryVO> list = dataResult.getData().getList();
        LocalDateTime now = LocalDateTime.now();
        list.forEach(item -> {
            // 是否可以删除  true代表有使用该场次的秒杀活动，不能删除，反之可以删除
            Result<Boolean> canDel = seckillEventClient.canDel(item.getId(), now);
            Boolean data = canDel.getData();
            if(data) {
                item.setDel(0);
            } else {
                item.setDel(1);
            }
        });
        return dataResult;
    }

    public Result<String> add(CateringAdmin admin, MarketingSeckillEventAddDTO dto) {
        // 设置当前新增操作人信息, 以及创建时间
        dto.setCreateBy(admin.getId());
        dto.setCreateTime(LocalDateTime.now());
        return seckillEventClient.add(dto);
    }

    public Result<String> edit(CateringAdmin admin, MarketingSeckillEventEditDTO dto) {
        // 设置当前编辑操作人信息, 以及创建时间
        dto.setUpdateBy(admin.getId());
        dto.setUpdateTime(LocalDateTime.now());
        return seckillEventClient.edit(dto);
    }

    public Result<String> del(Long eventId) {
        return seckillEventClient.del(eventId);
    }

    public Result<MarketingSeckillEventDetailVO> detail(Long eventId) {
        Result<CateringMarketingSeckillEventEntity> result = seckillEventClient.detail(eventId);
        CateringMarketingSeckillEventEntity entity = result.getData();
        MarketingSeckillEventDetailVO vo = new MarketingSeckillEventDetailVO();
        vo.setId(entity.getId());

        vo.setBeginTime(entity.getBeginTime().toString());
        vo.setEndTime(entity.getEndTime().toString());
        return Result.succ(vo);
    }
}
