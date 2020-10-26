package com.meiyuan.catering.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.ServiceResultCodeEnums;
import com.meiyuan.catering.allinpay.enums.service.DeviceTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.*;
import com.meiyuan.catering.allinpay.model.param.order.AllinPayAgentCollectApplyParams;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayAgentCollectApplyResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayRefundResult;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.AllinpayException;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.entity.CateringAllinRefundOrderEntity;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.feign.AllinRefundOrderClint;
import com.meiyuan.catering.order.feign.OrderTransactionFlowClient;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.OrderQueryResult;
import com.meiyuan.catering.pay.dto.pay.WxPayDto;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import com.meiyuan.catering.pay.service.MyPayService;
import com.meiyuan.catering.pay.util.AllinHelper;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author GongJunZheng
 * @date 2020/09/28 13:09
 * @description 通联支付服务实现
 **/

@Slf4j
@Service("myAllinPayServiceImpl")
public class MyAllinPayServiceImpl implements MyPayService {

    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private IOrderService iOrderService;
    @Resource
    private OrderTransactionFlowClient flowClient;
    @Autowired
    private AllinHelper allinHelper;
    @Autowired
    private AllinRefundOrderClint allinRefundOrderClint;

    @Override
    public Object pay(BasePayDto dto) {
        log.debug("微信支付开始..");
        WxPayDto payDto = (WxPayDto) dto;
        // 元转成分
        int totalFee = PayUtil.yuanToFen(dto.getPayFee());
        int fee = PayUtil.yuanToFen(payDto.getFee());

        NotifyEnum notifyEnum = payDto.getNotifyEnum();

        PayMethodKeyEnums payMethodEnum = payDto.getPayMethod();

        // 支付方式
        JSONObject payMethod = PayUtil.builderPayMethod(payMethodEnum, iOrderService, payDto.getOpenId(), totalFee);

        AllinPayAgentCollectApplyParams params = AllinPayAgentCollectApplyParams.builder()
                .bizOrderNo(payDto.getOrderNumber())
                .payerId(payDto.getUserId().toString())
                .recieverList(payDto.getReceivers())
                .tradeCode(TradeCodeEnums.E_COMMERCE_AGENT_COLLECT.getCode())
                .amount(totalFee)
                .fee(fee)
                .backUrl(PayUtil.getNotifyUrl(iOrderService, notifyEnum))
                .payMethod(payMethod)
                .validateType(ValidateTypeEnums.NO_VALIDATE.getType())
                .industryCode(IndustryEnums.CATERING_PLATFORM.getCode())
                .industryName(IndustryEnums.CATERING_PLATFORM.getName())
                .source(DeviceTypeEnums.PC.getType())
                .extendInfo(payDto.getBody())
                .build();

        try {
            AllinPayAgentCollectApplyResult result = iOrderService.agentCollectApply(params);
            return PayUtil.builderPayResult(payMethodEnum, result);
        } catch (AllinpayException e) {

            String code = e.getCode();
            if (ServiceResultCodeEnums.RESTRICT_NUMBER.getCode().equals(code)
                    || ServiceResultCodeEnums.RESTRICT_TOTAL_AMOUNT.getCode().equals(code)
            ) {
                throw new CustomException("为了您的账户安全,今日微信支付关闭");
            }

            if (ServiceResultCodeEnums.RESTRICT_AMOUNT.getCode().equals(code)) {
                throw new CustomException("支付金额过大,不能使用微信支付");
            }

            throw e;
        }

    }

    @Override
    public void cancel(Order order) {
        log.debug("通联取消开始..");

        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(order.getStoreId());
        //调用通联退款
        AllinPayRefundResult result = PayUtil.allinPayRefund(order, FinanceUtil.refundNo(shop.getShopCode()));

        log.debug("result:{}", result);

        //验证退款结果,如果失败保存错误信息
        verifyRefundResult(result, order.getId(), order.getTradingFlow(), order.getStoreId(), order.getMemberId());

        //退款交易流水号
        String refundId = result.getOrderNo();
        //退款响应信息
        String jsonString = JSONObject.toJSONString(result);
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(order, jsonString, refundId, PayWayEnum.parse(order.getPayWay()), TransactionFlowEnum.REFUND);
        //删除分账信息
        allinHelper.deleteSplitBill(order.getId(), order.getStoreId());
        log.debug("通联取消结束..");
    }

    @Override
    public void asyncCancel(Order order) {
        cancel(order);
    }

    @Override
    public void refund(RefundOrder refundOrder) {
        log.debug("通联微信退款开始..");

        //调用通联退款
        AllinPayRefundResult result = PayUtil.allinPayRefund(refundOrder);

        log.debug("result:{}", result);

        //验证退款结果,如果失败保存错误信息
        verifyRefundResult(result, refundOrder.getOrderId(), refundOrder.getTradingFlow(), refundOrder.getStoreId(), refundOrder.getMemberId());

        //退款交易流水号
        String refundId = result.getOrderNo();
        //退款响应信息
        String jsonString = JSONObject.toJSONString(result);
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(refundOrder, jsonString, refundId, PayWayEnum.WX_ALLIN);
        //删除分账信息
        allinHelper.deleteSplitBill(refundOrder.getOrderId(), refundOrder.getStoreId());
        log.debug("通联微信退款结束..");
    }


    @Override
    public void closeOrder(Order order) {
        String orderNumber = order.getOrderNumber();
        log.debug("{}:通联关闭订单开始..", orderNumber);
        AllinPayOrderDetailResult result = PayUtil.allinQueryOrder(orderNumber);
        //支付成功
        if (OrderStatusEnums.TRANSACTION_SUCC.getStatus().equals(result.getOrderStatus())) {
            order.setPaidAmount(order.getDiscountLaterFee());
            //调用通联退款
            AllinPayRefundResult refundResult = PayUtil.allinPayRefund(order, FinanceUtil.refundNo("TK"));


            //验证退款结果,如果失败保存错误信息
            verifyRefundResult(refundResult, order.getId(), order.getTradingFlow(), order.getStoreId(), order.getMemberId());

            log.debug("通联关闭订单refundResult:{}", refundResult);
        }
        log.debug("{}:通联关闭订单结束..", orderNumber);
    }

    @Override
    public OrderQueryResult queryOrder(String tradingFlow) {
        return null;
    }


    private void verifyRefundResult(AllinPayRefundResult result, Long orderId, String tradingFlow, Long storeId, Long memberId) {

        if (PayStatusEnums.FAIL.getCode().equals(result.getPayStatus())) {
            //保存确认退款信息
            CateringAllinRefundOrderEntity orderEntity = new CateringAllinRefundOrderEntity();
            orderEntity.setOrderId(orderId);
            orderEntity.setTradingFlow(tradingFlow);
            orderEntity.setStoreId(storeId);
            orderEntity.setMemberId(memberId);
            orderEntity.setAmount(result.getAmount());
            orderEntity.setStatus(1);
            orderEntity.setRemarks(result.getPayFailMessage());

            allinRefundOrderClint.save(orderEntity);
        }
    }

}
