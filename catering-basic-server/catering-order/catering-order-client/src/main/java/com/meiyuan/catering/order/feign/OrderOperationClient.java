package com.meiyuan.catering.order.feign;


import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.enums.OrderOperationTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderOperationClient {
    @Resource
    private CateringOrdersOperationService service;


    /**
     * 描述:同步添加
     *
     * @param order
     * @param explain
     * @param operationEnum
     * @param typeEnum
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:17
     */
    public Result saveOperation(Order order, String explain, OrderOperationEnum operationEnum, OrderOperationTypeEnum typeEnum) {
        service.saveOperation(order, explain, operationEnum, typeEnum);
        return Result.succ();
    }

}
