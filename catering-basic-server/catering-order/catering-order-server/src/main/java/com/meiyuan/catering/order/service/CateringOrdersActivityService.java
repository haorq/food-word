package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponOrderAmountCountVO;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import com.meiyuan.catering.marketing.vo.special.MarketingSpecialEffectGoodsCountVO;
import com.meiyuan.catering.order.entity.CateringOrdersActivityEntity;
import com.meiyuan.catering.order.vo.marketing.MarketingOrderGoodsCountVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 订单活动表(CateringOrdersActivity)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-26 18:05:12
 */
public interface CateringOrdersActivityService extends IService<CateringOrdersActivityEntity> {
    /**
     * 描述:团购订单id查询
     *
     * @param orderId
     * @return com.meiyuan.catering.order.entity.CateringOrdersActivityEntity
     * @author zengzhangni
     * @date 2020/4/1 17:46
     */
    List<CateringOrdersActivityEntity> getByOrderId(Long orderId);

    /**
    * 营销活动关联订单营业金额统计
    * @param marketingIds 营销活动ID集合
    * @author: GongJunZheng
    * @date: 2020/8/5 16:23
    * @return: {@link List<MarketingSeckillGrouponOrderAmountCountVO>}
    **/
    List<MarketingSeckillGrouponOrderAmountCountVO> marketingOrderAmountCount(List<Long> marketingIds);

    /**
     * 通过营销ID统计关联订单数
     * @param marketingId 营销ID
     * @author: GongJunZheng
     * @date: 2020/8/7 15:14
     * @return: {@link Result<Integer>}
     **/
    Integer marketingRelationOrderCount(Long marketingId);

    /**
    * 通过营销活动ID查询秒杀商品销售额
    * @param marketingId 营销活动ID
    * @author: GongJunZheng
    * @date: 2020/8/8 15:12
    * @return: {@link List<MarketingOrderGoodsCountVo>}
    * @version V1.3.0
    **/
    List<MarketingOrderGoodsCountVo> marketingGoodsBusinessCount(Long marketingId);

    /**
     * 通过营销活动ID查询实际增长营业额
     * @param marketingId 营销商品ID
     * @author: GongJunZheng
     * @date: 2020/8/8 19:02
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal marketingRealBusinessCount(Long marketingId);

    /**
     * 根据订单ID获取订单活动信息
     * @param orderId 订单ID
     * @param activityId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/17 16:38
     * @return: {@link List<CateringOrdersActivityEntity>}
     * @version V1.3.0
     **/
    List<CateringOrdersActivityEntity> listByOrderId(Long orderId, Long activityId);

    /**
     * 查询指定秒杀活动的营销商品实际成本
     * @param seckillId 秒杀活动ID
     * @param skuCodeSet 营销商品SKU集合
     * @param seckillEventIdSet 秒杀场次ID集合
     * @param type 是否查询已售后订单 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/19 15:50
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal seckillRealCostCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet, Integer type);

    /**
     * 处理老数据查询指定秒杀活动的营销商品实际成本
     * @param seckillId 秒杀活动ID
     * @param type 是否查询已售后订单 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/19 15:50
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal oldSeckillRealCostCount(Long seckillId, Integer type);

    /**
     * 查询指定秒杀活动每个营销商品的销售额
     * @param seckillId 秒杀活动ID
     * @param skuCodeSet 营销商品SKU集合
     * @param seckillEventIdSet 秒杀场次ID集合
     * @author: GongJunZheng
     * @date: 2020/8/19 16:03
     * @return: {@link List<MarketingOrderGoodsCountVo>}
     * @version V1.3.0
     **/
    List<MarketingOrderGoodsCountVo> seckillGoodsBusinessCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet);

    /**
     * 处理老数据查询指定秒杀活动每个营销商品的销售额
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/19 16:03
     * @return: {@link List<MarketingOrderGoodsCountVo>}
     * @version V1.3.0
     **/
    List<MarketingOrderGoodsCountVo> oldSeckillGoodsBusinessCount(Long seckillId);

    /**
     * 查询指定团购活动的实际成本
     * @param grouponId 团购活动ID
     * @param type 查询是否包括售后 0-否 1-是
     * @author: GongJunZheng
     * @date: 2020/8/19 16:50
     * @return: {@link BigDecimal}
     * @version V1.3.0
     **/
    BigDecimal grouponRealCostCount(Long grouponId, Integer type);

    /**
     * 查询指定团购活动每个营销商品的销售额
     * @param grouponId 团购活动ID
     * @author: GongJunZheng
     * @date: 2020/8/19 17:02
     * @return: {@link List<MarketingOrderGoodsCountVo>}
     * @version V1.3.0
     **/
    List<MarketingOrderGoodsCountVo> grouponGoodsBusinessCount(Long grouponId);

    /**
     * 查询秒杀活动的商品销售情况
     * @param seckillId 秒杀活动ID
     * @param skuCodeSet 秒杀商品SKU集合
     * @param seckillEventIdSet 秒杀活动场次ID集合
     * @author: GongJunZheng
     * @date: 2020/8/24 13:16
     * @return: {@link List<MarketingRepertoryGoodsSoldVo>}
     * @version V1.3.0
     **/
    List<MarketingRepertoryGoodsSoldVo> seckillSoldCount(Long seckillId, Set<String> skuCodeSet, Set<Long> seckillEventIdSet);

    /**
     * 查询老数据秒杀活动的商品销售情况
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/24 13:16
     * @return: {@link List<MarketingRepertoryGoodsSoldVo>}
     * @version V1.3.0
     **/
    List<MarketingRepertoryGoodsSoldVo> oldSeckillSoldCount(Long seckillId);

    /**
     * 查询指定的营销特价商品活动的实际成本
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 17:53
     * @return: {@link BigDecimal}
     * @version V1.4.0
     **/
    BigDecimal specialRealCostCount(Long specialId);

    /**
     * 查询营销特价商品活动的商品销售额统计
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/3 18:04
     * @return: {@link BigDecimal}
     * @version V1.4.0
     **/
    BigDecimal specialBusinessCostCount(Long specialId);

    /**
     * 统计营销特价商品订单数据 关联订单量 销售数量 销售额
     * @param specialId 营销特价商品活动ID
     * @param specialGoodsSkuList 营销特价商品SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/9/3 18:48
     * @return: {@link List<MarketingSpecialEffectGoodsCountVO>}
     * @version V1.4.0
     **/
    List<MarketingSpecialEffectGoodsCountVO> effectGoodsCount(Long specialId, List<String> specialGoodsSkuList);

    /**
     * 查询指定营销特价商品活动的实际增长营业额
     * @param specialId 营销特价商品活动ID
     * @author: GongJunZheng
     * @date: 2020/9/14 15:26
     * @return: {@link BigDecimal}
     * @version V1.4.0
     **/
    BigDecimal specialRealBusinessCount(Long specialId);
}
