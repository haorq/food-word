package com.meiyuan.catering.pay.service.impl;

import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.feign.OrderTransactionFlowClient;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.OrderQueryResult;
import com.meiyuan.catering.pay.service.MyPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengzhangni
 * @date 2020/9/29 14:58
 * @since v1.1.0
 */
@Service("myZeroPayServiceImpl")
@Slf4j
public class MyZeroPayServiceImpl implements MyPayService {
    @Resource
    private OrderTransactionFlowClient flowClient;

    @Override
    public Object pay(BasePayDto dto) {
        return null;
    }

    @Override
    public void cancel(Order order) {
        log.debug("O元取消开始..");
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(order, "", FinanceUtil.balanceRefundTransactionNo(), PayWayEnum.ZERO_PAY, TransactionFlowEnum.REFUND);
        log.debug("O元取消结束..");
    }

    @Override
    public void asyncCancel(Order order) {
        cancel(order);
    }

    @Override
    public void refund(RefundOrder refundOrder) {
        log.debug("O元退款开始..");
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(refundOrder, "", FinanceUtil.balanceRefundTransactionNo(), PayWayEnum.BALANCE);
        log.debug("O元退款结束..");
    }

    @Override
    public void closeOrder(Order order) {
    }

    @Override
    public OrderQueryResult queryOrder(String tradingFlow) {
        return null;
    }

}
