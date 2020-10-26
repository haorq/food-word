package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.marketing.dao.CateringMarketingPullNewMapper;
import com.meiyuan.catering.marketing.dao.CateringMarketingSeckillMapper;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.enums.MarketingStatusEnum;
import com.meiyuan.catering.marketing.enums.MarketingTypeEnum;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingGrouponService;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillService;
import com.meiyuan.catering.marketing.service.CateringMarketingService;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.pullnew.MarketingPullNewCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 统一的营销活动Service服务层实现
 **/

@Slf4j
@Service("cateringMarketingService")
public class CateringMarketingServiceImpl implements CateringMarketingService {

    @Resource
    private CateringMarketingSeckillMapper seckillMapper;
    @Resource
    private CateringMarketingPullNewMapper pullNewMapper;
    @Autowired
    private CateringMarketingSeckillService seckillService;
    @Autowired
    private CateringMarketingGrouponService grouponService;

    @Override
    public PageData<MarketingSeckillGrouponPageQueryVO> pageQuery(MarketingSeckillGrouponPageQueryDTO dto) {
        // 补全条件
        dto.setNow(LocalDateTime.now());
        // 默认未开始
        dto.setMarketingType(null == dto.getMarketingType() ? MarketingTypeEnum.ALL.getStatus() : dto.getMarketingType());
        // 默认全部
        dto.setMarketingStatus(null == dto.getMarketingStatus() ? MarketingStatusEnum.ING.getStatus() : dto.getMarketingStatus());
        if(MarketingStatusEnum.ING.getStatus().equals(dto.getMarketingStatus())) {
            return pageQueryHaving(dto);
        } else if(MarketingStatusEnum.NO_BEGIN.getStatus().equals(dto.getMarketingStatus())) {
            return pageQueryNoBegin(dto);
        } else {
            return pageQueryEnd(dto);
        }
    }

    /**
     * 查询进行中的营销活动（秒杀/团购）列表分页信息
     */
    private PageData<MarketingSeckillGrouponPageQueryVO> pageQueryHaving(MarketingSeckillGrouponPageQueryDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingSeckillGrouponPageQueryVO> pageData = seckillMapper.marketingActivityHavingPageQuery(pageCondition, dto);
        if (!BaseUtil.judgeList(pageData.getRecords())) {
            return new PageData<>(pageData);
        }
        LocalDateTime now = LocalDateTime.now();
        pageData.getRecords().forEach(item -> {
            // 补充活动剩余时间，计算时间差（单位：分）
            Duration duration = Duration.between(now, item.getEndTime());
            item.setOverPlusTime(duration.toMinutes());
            // 统一设置成进行中
            item.setMarketingStatus(MarketingStatusEnum.ING.getStatus());
        });
        return new PageData<>(pageData);
    }

    /**
     * 查询未开始的营销活动（秒杀/团购）列表分页信息
     */
    private PageData<MarketingSeckillGrouponPageQueryVO> pageQueryNoBegin(MarketingSeckillGrouponPageQueryDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingSeckillGrouponPageQueryVO> pageData = seckillMapper.marketingActivityNoBeginPageQuery(pageCondition, dto);
        if (!BaseUtil.judgeList(pageData.getRecords())) {
            return new PageData<>(pageData);
        }
        pageData.getRecords().forEach(item ->
            // 统一设置成未开始
            item.setMarketingStatus(MarketingStatusEnum.NO_BEGIN.getStatus())
        );
        return new PageData<>(pageData);
    }

    /**
     * 查询已结束的营销活动（秒杀/团购）列表分页信息
     */
    private PageData<MarketingSeckillGrouponPageQueryVO> pageQueryEnd(MarketingSeckillGrouponPageQueryDTO dto) {
        // 开启分页
        Page<Object> pageCondition = new Page<>(dto.getPageNo(), dto.getPageSize());
        IPage<MarketingSeckillGrouponPageQueryVO> pageData = seckillMapper.marketingActivityEndPageQuery(pageCondition, dto);
        if (!BaseUtil.judgeList(pageData.getRecords())) {
            return new PageData<>(pageData);
        }
        pageData.getRecords().forEach(item -> {
            // upDown = 1是冻结结束状态，upDown = 2是正常结束状态，
            if(MarketingUpDownStatusEnum.UP.getStatus().equals(item.getUpDown())) {
                item.setMarketingStatus(MarketingStatusEnum.END.getStatus());
            } else if(MarketingUpDownStatusEnum.DOWN.getStatus().equals(item.getUpDown())){
                item.setMarketingStatus(MarketingStatusEnum.FREEZE.getStatus());

            }
        });
        return new PageData<>(pageData);
    }

    private String buildMarketingTarget(Integer userTarget, BigDecimal businessTarget) {
        return String.format("拉新%s位新用户，增长%s营业额", userTarget, businessTarget);
    }

    @Override
    public Map<Long, MarketingPullNewCountVO> pullNewCount(List<Long> marketingIds) {
        if(!BaseUtil.judgeList(marketingIds)) {
            // 没有活动ID集合，直接返回空MAP
            return Collections.emptyMap();
        }
        List<MarketingPullNewCountVO> pullNewCountList = pullNewMapper.pullNewCount(marketingIds);
        if (!BaseUtil.judgeList(pullNewCountList)) {
            return new HashMap<>(16);
        }
        return pullNewCountList.stream().collect(Collectors.toMap(MarketingPullNewCountVO::getOfId, Function.identity()));
    }


    @Override
    public void shopDelSync(Long shopId) {
        // 团购活动
        grouponService.shopDelSync(shopId);
        // 秒杀活动
        seckillService.shopDelSync(shopId);
    }
}
