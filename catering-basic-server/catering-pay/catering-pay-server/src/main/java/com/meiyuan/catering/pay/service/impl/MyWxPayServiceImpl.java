package com.meiyuan.catering.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.FinanceUtil;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.enums.TransactionFlowEnum;
import com.meiyuan.catering.order.feign.OrderTransactionFlowClient;
import com.meiyuan.catering.pay.dto.BasePayDto;
import com.meiyuan.catering.pay.dto.OrderQueryResult;
import com.meiyuan.catering.pay.dto.pay.WxPayDto;
import com.meiyuan.catering.pay.service.MyPayService;
import com.meiyuan.catering.pay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/4/2
 */
@Service("myWxPayServiceImpl")
@Slf4j
public class MyWxPayServiceImpl implements MyPayService {

    @Resource
    private OrderTransactionFlowClient flowClient;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayConfig wxPayConfig;
    @Resource
    private MerchantUtils merchantUtils;

    @Override
    public Object pay(BasePayDto dto) {
        log.debug("微信支付开始..");
        WxPayDto payDto = (WxPayDto) dto;
        // 元转成分
        int fee = PayUtil.yuanToFen(dto.getPayFee());
        WxPayMpOrderResult result = null;
        if (fee > 0) {
            try {
                String orderNumber = payDto.getOrderNumber();
                WxPayUnifiedOrderRequest orderRequest = WxPayUnifiedOrderRequest.newBuilder()
                        .outTradeNo(orderNumber)
                        .openid(payDto.getOpenId())
                        .body(payDto.getBody())
                        .totalFee(fee)
                        .attach(payDto.getAttach())
                        .notifyUrl(wxPayConfig.getNotifyUrl() + payDto.getNotifyEnum().getNotifyUrl())
                        .spbillCreateIp(payDto.getIp()).build();

                result = wxPayService.createOrder(orderRequest);
            } catch (WxPayException e) {
                log.error("付款订单的预支付会话标识失败：{}", e.getXmlString());
                throw new CustomException("预支付失败");
            }
        }
        log.debug("微信支付结束..");
        return result;
    }

    @Override
    public void cancel(Order order) {
        log.debug("微信取消开始..");
        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(order.getStoreId());
        //调用微信退款
        WxPayRefundResult wxPayRefundResult = PayUtil.wxPayRefund(order.getTradingFlow(), FinanceUtil.refundNo(shop.getShopCode()), order.getPaidAmount());
        //退款交易流水号
        String refundId = wxPayRefundResult.getRefundId();
        //退款响应信息
        String jsonString = JSONObject.toJSONString(wxPayRefundResult);
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(order, jsonString, refundId, PayWayEnum.WX, TransactionFlowEnum.REFUND);
        log.debug("微信取消结束..");
    }

    @Override
    public void asyncCancel(Order order) {
        cancel(order);
    }

    @Override
    public void refund(RefundOrder refundOrder) {
        log.debug("微信退款开始..");
        BigDecimal refundAmount = refundOrder.getRefundAmount();
        //调用微信退款
        WxPayRefundResult wxPayRefundResult = PayUtil.wxPayRefund(refundOrder.getTradingFlow(), refundOrder.getRefundNumber(), refundAmount);
        //退款交易流水号
        String refundId = wxPayRefundResult.getRefundId();
        //退款响应信息
        String jsonString = JSONObject.toJSONString(wxPayRefundResult);
        //保存退款流水
        flowClient.saveOrdersTransactionFlow(refundOrder, jsonString, refundId, PayWayEnum.WX);
        log.debug("微信退款结束..");
    }

    @Override
    public void closeOrder(Order order) {
        String orderNumber = order.getOrderNumber();
        log.debug("{}:微信关闭订单开始..", orderNumber);
        WxPayOrderQueryResult result = PayUtil.wxQueryOrder(order.getTradingFlow());
        //支付成功
        if (Objects.equals(result.getTradeState(), PayUtil.SUCCESS)) {
            order.setPaidAmount(order.getDiscountLaterFee());
            cancel(order);
        }
        log.debug("{}:微信关闭订单结束..", orderNumber);
    }


    @Override
    public OrderQueryResult queryOrder(String tradingFlow) {
        WxPayOrderQueryResult result = PayUtil.wxQueryOrder(tradingFlow);
        return OrderQueryResult.builder().wxPayOrderResult(result).build();
    }
}
