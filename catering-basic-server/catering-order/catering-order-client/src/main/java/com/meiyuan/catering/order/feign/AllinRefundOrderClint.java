package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.order.entity.CateringAllinRefundOrderEntity;
import com.meiyuan.catering.order.service.CateringAllinRefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengzhangni
 * @date 2020/10/20 19:28
 * @since v1.5.0
 */
@Service
public class AllinRefundOrderClint {

    @Autowired
    private CateringAllinRefundOrderService cateringAllinRefundOrderService;

    public void save(CateringAllinRefundOrderEntity orderEntity) {
        cateringAllinRefundOrderService.save(orderEntity);
    }
}
