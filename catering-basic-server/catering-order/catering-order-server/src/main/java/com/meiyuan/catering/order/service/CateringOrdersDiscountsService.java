package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;

/**
 * 订单优惠信息表(CateringOrdersDiscounts)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersDiscountsService extends IService<CateringOrdersDiscountsEntity> {

    /**
     * 描述:查询订单优惠卷id
     *
     * @param orderId
     * @return java.lang.Long
     * @author zengzhangni
     * @date 2020/5/20 10:55
     * @since v1.1.0
     */
    Long getDiscountId(Long orderId);



}
