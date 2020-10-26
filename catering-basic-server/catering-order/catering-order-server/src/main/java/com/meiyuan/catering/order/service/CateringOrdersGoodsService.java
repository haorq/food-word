package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.order.entity.CateringOrdersGoodsEntity;

import java.util.List;

/**
 * 订单商品表(CateringOrdersGoods)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:03
 */
public interface CateringOrdersGoodsService extends IService<CateringOrdersGoodsEntity> {
    /**
     * 描述:通过订单id查询订单商品信息
     *
     * @param orderId
     * @return java.util.List<OrderGoods>
     * @author zengzhangni
     * @date 2020/5/20 11:10
     * @since v1.1.0
     */
    List<OrderGoods> getByOrderId(Long orderId);

}
