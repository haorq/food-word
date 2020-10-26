package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.PageUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventAddDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventEditDTO;
import com.meiyuan.catering.marketing.dto.seckillevent.MarketingSeckillEventPageQueryDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillEventService;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventPageQueryVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 秒杀场次Client
 **/

@Slf4j
@Service
public class MarketingSeckillEventClient {

    @Autowired
    private CateringMarketingSeckillEventService seckillEventService;

    public Result<PageData<MarketingSeckillEventPageQueryVO>> pageQuery(MarketingSeckillEventPageQueryDTO dto) {
        PageData<CateringMarketingSeckillEventEntity> pageData = seckillEventService.pageQuery(dto);
        // 数据转化
        PageData<MarketingSeckillEventPageQueryVO> pageResult = new PageData<>();
        pageResult.setTotal(pageData.getTotal());
        pageResult.setLastPage(PageUtil.lastPages(pageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        pageResult.setList(BaseUtil.noNullAndListToList(pageData.getList(), MarketingSeckillEventPageQueryVO.class));
        log.info("秒杀场次列表分页查询获取的数据：{}", pageResult);
        return Result.succ(pageResult);
    }

    public Result<String> add(MarketingSeckillEventAddDTO dto) {
        // 数据转化
        CateringMarketingSeckillEventEntity seckillEventEntity = BaseUtil.objToObj(dto, CateringMarketingSeckillEventEntity.class);
        Boolean result = seckillEventService.addOrEdit(seckillEventEntity);
        return result ? Result.succ("新增成功") : Result.fail("新增失败");
    }

    public Result<String> edit(MarketingSeckillEventEditDTO dto) {
        // 数据转化
        CateringMarketingSeckillEventEntity seckillEventEntity = BaseUtil.objToObj(dto, CateringMarketingSeckillEventEntity.class);
        Boolean result = seckillEventService.addOrEdit(seckillEventEntity);
        return result ? Result.succ("编辑成功") : Result.fail("编辑失败");
    }

    public Result<String> del(Long eventId) {
        Boolean result = seckillEventService.del(eventId);
        return result ? Result.succ("删除成功") : Result.fail("删除失败");
    }

    public Result<Boolean> canDel(Long eventId, LocalDateTime dateTime) {
        return Result.succ(seckillEventService.canDel(eventId, dateTime));
    }

    public Result<CateringMarketingSeckillEventEntity> detail(Long eventId) {
        return Result.succ(seckillEventService.get(eventId));
    }

    public Result<List<CateringMarketingSeckillEventEntity>> list() {
        return Result.succ(seckillEventService.entityList());
    }

    /**
    * 根据秒杀场次ID集合查询秒杀场次信息集合
    * @param eventIds 秒杀场次ID集合
    * @author: GongJunZheng
    * @date: 2020/9/2 16:21
    * @return: {@link List<CateringMarketingSeckillEventEntity>}
    * @version V1.4.0
    **/
    public Result<List<CateringMarketingSeckillEventEntity>> selectListByIds(Set<Long> eventIds) {
        return Result.succ(seckillEventService.selectListByIds(eventIds));
    }
}
