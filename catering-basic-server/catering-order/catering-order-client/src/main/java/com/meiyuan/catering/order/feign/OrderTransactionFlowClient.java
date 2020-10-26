package com.meiyuan.catering.order.feign;

import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.service.CateringOrdersTransactionFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/5/19 17:00
 * @since v1.1.0
 */
@Service
public class OrderTransactionFlowClient {

    @Resource
    private CateringOrdersTransactionFlowService service;

    /**
     * 描述:添加订单交易流水
     *
     * @param order
     * @param jsonString
     * @param transactionId
     * @param payWayEnum
     * @param flowEnum
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 17:05
     * @since v1.1.0
     */
    public Result<Boolean> saveOrdersTransactionFlow(Order order, String jsonString, String transactionId, PayWayEnum payWayEnum, TransactionFlowEnum flowEnum) {
        return Result.succ(service.saveOrdersTransactionFlow(order, jsonString, transactionId, payWayEnum, flowEnum));
    }

    /**
     * 描述:添加退款交易流水
     *
     * @param refundOrder
     * @param jsonString
     * @param transactionId
     * @param payWayEnum
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 17:11
     * @since v1.1.0
     */
    public Result<Boolean> saveOrdersTransactionFlow(RefundOrder refundOrder, String jsonString, String transactionId, PayWayEnum payWayEnum) {
        return Result.succ(service.saveOrdersTransactionFlow(refundOrder, jsonString, transactionId, payWayEnum));
    }
}
