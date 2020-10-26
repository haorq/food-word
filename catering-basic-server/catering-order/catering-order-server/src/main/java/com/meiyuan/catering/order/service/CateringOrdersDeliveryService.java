package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryEntity;

/**
 * 订单配送表(CateringOrdersDelivery)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersDeliveryService extends IService<CateringOrdersDeliveryEntity> {

    /**
     * 描述:通过订单id查询配送信息
     *
     * @param orderId
     * @return com.meiyuan.catering.core.dto.order.delivery.OrderDelivery
     * @author zengzhangni
     * @date 2020/5/20 11:04
     * @since v1.1.0
     */
    OrderDelivery getByOrderId(Long orderId);

}
