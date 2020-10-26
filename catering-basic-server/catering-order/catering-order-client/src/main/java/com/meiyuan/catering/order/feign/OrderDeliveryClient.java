package com.meiyuan.catering.order.feign;


import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryEntity;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderDeliveryClient {
    @Resource
    private CateringOrdersDeliveryService service;


    /**
     * 描述:查询配送信息
     *
     * @param id
     * @return com.meiyuan.catering.core.dto.order.delivery.OrderDelivery
     * @author zengzhangni
     * @date 2020/5/20 11:04
     * @since v1.1.0
     */
    public Result<OrderDelivery> getByOrderId(Long id) {
        return Result.succ(service.getByOrderId(id));
    }


    /**
     * 更新订单配送表
     *
     * @param cateringOrdersDeliveryEntity
     * @return true:成功
     * @author lh
     * @date 17:20 2020/10/15
     */
    public Result<Boolean> updateOrderDelivery(CateringOrdersDeliveryEntity cateringOrdersDeliveryEntity){
        boolean result = service.updateById(cateringOrdersDeliveryEntity);
        return Result.succ(result);
    }
}
