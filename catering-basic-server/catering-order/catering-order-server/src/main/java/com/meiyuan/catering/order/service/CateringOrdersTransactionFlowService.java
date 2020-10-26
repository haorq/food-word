package com.meiyuan.catering.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.order.entity.CateringOrdersTransactionFlowEntity;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;

/**
 * 订单交易流水表(CateringOrdersTransactionFlow)服务层
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:04
 */
public interface CateringOrdersTransactionFlowService extends IService<CateringOrdersTransactionFlowEntity> {

    /**
     * 描述:添加订单交易流水
     *
     * @param order
     * @param xmlResult
     * @param transactionId
     * @param payWayEnum
     * @param flowEnum
     * @return com.meiyuan.catering.core.util.Result<java.lang.Boolean>
     * @author zengzhangni
     * @date 2020/5/19 17:05
     * @since v1.1.0
     */
    Boolean saveOrdersTransactionFlow(Order order, String xmlResult, String transactionId, PayWayEnum payWayEnum, TransactionFlowEnum flowEnum);


    /**
     * 描述:添加退款交易流水
     *
     * @param refundOrder
     * @param xmlResult
     * @param transactionId
     * @param payWayEnum
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/5/19 17:07
     * @since v1.1.0
     */
    Boolean saveOrdersTransactionFlow(RefundOrder refundOrder, String xmlResult, String transactionId, PayWayEnum payWayEnum);
}
