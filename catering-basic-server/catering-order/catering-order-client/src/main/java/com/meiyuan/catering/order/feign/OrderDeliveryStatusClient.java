package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.entity.CateringOrderDeliveryStatusHistoryEntity;
import com.meiyuan.catering.order.service.OrderDeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author lh
 */
@Service
public class OrderDeliveryStatusClient {

    @Autowired
    private OrderDeliveryStatusService orderDeliveryStatusService;

    /**
     * 达达配送状态回调
     *
     * @param entity
     * @return
     */
    public Result add(CateringOrderDeliveryStatusHistoryEntity entity) {
        entity.setCreateTime(DateTimeUtil.now());
        orderDeliveryStatusService.save(entity);
        return Result.succ();
    }
}
