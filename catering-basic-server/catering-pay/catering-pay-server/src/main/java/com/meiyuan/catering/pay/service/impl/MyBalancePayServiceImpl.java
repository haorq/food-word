package com.meiyuan.catering.pay.service.impl;

import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.finance.feign.UserBalanceAccountClient;
import com.meiyuan.catering.finance.feign.UserBalanceAccountRecordClient;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.feign.OrderTransactionFlowClient;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.OrderQueryResult;
import com.meiyuan.catering.pay.service.MyPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/4/2
 */
@Service("myBalancePayServiceImpl")
@Slf4j
public class MyBalancePayServiceImpl implements MyPayService {

    @Autowired
    private UserBalanceAccountClient accountClient;
    @Autowired
    private UserBalanceAccountRecordClient accountRecordClient;
    @Resource
    private OrderTransactionFlowClient flowClient;

    @Override
    public Object pay(BasePayDto dto) {
        log.debug("余额支付开始..");
        Long memberId = dto.getUserId();
        BigDecimal laterFee = dto.getPayFee();
        //余额扣除
        accountClient.balanceSubtract(memberId, laterFee);
        log.debug("余额支付结束..");
        return null;
    }

    @Override
    public void cancel(Order order) {
        log.debug("余额取消开始..");
        Long userId = order.getMemberId();
        BigDecimal refundAmount = order.getPaidAmount();
        //余额退还
        accountClient.balanceAdd(userId, refundAmount);
        //添加余额账号明细
        accountRecordClient.saveBalanceAccountRecord(userId, refundAmount, "");
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(order, "", FinanceUtil.balanceRefundTransactionNo(), PayWayEnum.BALANCE, TransactionFlowEnum.REFUND);
        log.debug("余额取消结束..");
    }

    @Override
    public void asyncCancel(Order order) {
        cancel(order);
    }

    @Override
    public void refund(RefundOrder refundOrder) {
        log.debug("余额退款开始..");
        Long userId = refundOrder.getMemberId();
        BigDecimal refundAmount = refundOrder.getRefundAmount();
        //余额退还
        accountClient.balanceAdd(userId, refundAmount);

        //添加余额账号明细
        accountRecordClient.saveBalanceAccountRecord(userId, refundAmount, refundOrder.getRefundNumber());

        //保存退款流水
        flowClient.saveOrdersTransactionFlow(refundOrder, "", FinanceUtil.balanceRefundTransactionNo(), PayWayEnum.BALANCE);
        log.debug("余额退款结束..");
    }

    @Override
    public void closeOrder(Order order) {

    }

    @Override
    public OrderQueryResult queryOrder(String orderNumber) {
        return null;
    }

}
