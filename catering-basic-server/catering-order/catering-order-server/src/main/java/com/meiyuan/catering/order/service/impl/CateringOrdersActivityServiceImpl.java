package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponOrderAmountCountVO;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialEffectGoodsCountVO;
import com.meiyuan.catering.order.dao.CateringOrdersActivityMapper;
import com.meiyuan.catering.order.entity.CateringOrdersActivityEntity;
import com.meiyuan.catering.order.service.CateringOrdersActivityService;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 订单活动表(CateringOrdersActivity)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-26 18:06:14
 */
@Service("cateringOrdersActivityService")
public class CateringOrdersActivityServiceImpl extends ServiceImpl<CateringOrdersActivityMapper, CateringOrdersActivityEntity> implements CateringOrdersActivityService {

    @Resource
    private CateringOrdersActivityMapper activityMapper;

    @Override
    public List<CateringOrdersActivityEntity> getByOrderId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersActivityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersActivityEntity::getRelationId, orderId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<MarketingSeckillGrouponOrderAmountCountVO> marketingOrderAmountCount(List<Long> marketingIds) {
        return activityMapper.marketingOrderAmountCount(marketingIds);
    }

    @Override
    public Integer marketingRelationOrderCount(Long marketingId) {
        return activityMapper.marketingRelationOrderCount(marketingId);
    }

    @Override
    public List<MarketingOrderGoodsCountVo> marketingGoodsBusinessCount(Long marketingId) {
        return activityMapper.marketingGoodsBusinessCount(marketingId);
    }

    @Override
    public BigDecimal marketingRealBusinessCount(Long marketingId) {
        return activityMapper.marketingRealBusinessCount(marketingId);
    }

    @Override
    public List<CateringOrdersActivityEntity> listByOrderId(Long orderId, Long activityId) {
        LambdaQueryWrapper<CateringOrdersActivityEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersActivityEntity :: getRelationId, orderId)
                    .eq(CateringOrdersActivityEntity :: getActivityId, activityId);
        return list(queryWrapper);
    }

    @Override
    public BigDecimal seckillRealCostCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet, Integer type) {
        return activityMapper.seckillRealCostCount(seckillId, skuCodeSet, seckillEventIdSet, type);
    }

    @Override
    public BigDecimal oldSeckillRealCostCount(Long seckillId, Integer type) {
        return activityMapper.oldSeckillRealCostCount(seckillId, type);
    }

    @Override
    public List<MarketingOrderGoodsCountVo> seckillGoodsBusinessCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet) {
        return activityMapper.seckillGoodsBusinessCount(seckillId, skuCodeSet, seckillEventIdSet);
    }

    @Override
    public List<MarketingOrderGoodsCountVo> oldSeckillGoodsBusinessCount(Long seckillId) {
        return activityMapper.oldSeckillGoodsBusinessCount(seckillId);
    }

    @Override
    public BigDecimal grouponRealCostCount(Long grouponId, Integer type) {
        return activityMapper.grouponRealCostCount(grouponId, type);
    }

    @Override
    public List<MarketingOrderGoodsCountVo> grouponGoodsBusinessCount(Long grouponId) {
        return activityMapper.grouponGoodsBusinessCount(grouponId);
    }

    @Override
    public List<MarketingRepertoryGoodsSoldVo> seckillSoldCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet) {
        return activityMapper.seckillSoldCount(seckillId, skuCodeSet, seckillEventIdSet);
    }

    @Override
    public List<MarketingRepertoryGoodsSoldVo> oldSeckillSoldCount(Long seckillId) {
        return activityMapper.oldSeckillSoldCount(seckillId);
    }

    @Override
    public BigDecimal specialRealCostCount(Long specialId) {
        return activityMapper.specialRealCostCount(specialId);
    }

    @Override
    public BigDecimal specialBusinessCostCount(Long specialId) {
        return activityMapper.specialBusinessCostCount(specialId);
    }

    @Override
    public List<MarketingSpecialEffectGoodsCountVO> effectGoodsCount(Long specialId, List<String> specialGoodsSkuList) {
        return activityMapper.effectGoodsCount(specialId, specialGoodsSkuList);
    }

    @Override
    public BigDecimal specialRealBusinessCount(Long specialId) {
        return activityMapper.specialRealBusinessCount(specialId);
    }
}
