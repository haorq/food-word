package com.meiyuan.catering.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.meiyuan.catering.allinpay.autoconfigure.AllinPayConfig;
import com.meiyuan.catering.allinpay.enums.service.order.OrderRefundEnums;
import com.meiyuan.catering.allinpay.enums.service.order.OrderStatusEnums;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import com.meiyuan.catering.allinpay.model.bean.notify.NotifyResult;
import com.meiyuan.catering.allinpay.model.bean.paymethod.ScanWechatOrg;
import com.meiyuan.catering.allinpay.model.bean.paymethod.WechatPayMiniOrg;
import com.meiyuan.catering.allinpay.model.param.order.AllinPayOrderDetailParams;
import com.meiyuan.catering.allinpay.model.param.order.AllinPayRefundParams;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayAgentCollectApplyResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult;
import com.meiyuan.catering.allinpay.model.result.order.AllinPayRefundResult;
import com.meiyuan.catering.allinpay.model.result.order.WxMiniPayResult;
import com.meiyuan.catering.allinpay.service.BaseAllinPayService;
import com.meiyuan.catering.allinpay.service.IOrderService;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.enums.base.merchant.tl.TlAuditStatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.feign.ShopBankClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.merchant.vo.shop.bank.ShopBankInfoVo;
import com.meiyuan.catering.pay.enums.NotifyEnum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/4/1
 */
@Slf4j
public class PayUtil {

    public final static String SUCCESS = "SUCCESS";
    public final static BigDecimal B_100 = BigDecimal.valueOf(100);
    public final static BigDecimal B_3 = BigDecimal.valueOf(3);
    public final static BigDecimal B_6_8 = BigDecimal.valueOf(6.8);


    /**
     * 描述: 处理支付回调信息
     *
     * @param xmlResult
     * @return com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult
     * @author zengzhangni
     * @date 2020/3/31 17:23
     */
    public static WxPayOrderNotifyResult getNotifyInfo(WxPayService payService, String xmlResult) {
        try {
            log.debug("回调信息:{}", xmlResult);
            return payService.parseOrderNotifyResult(xmlResult);
        } catch (WxPayException e) {
            String errorMsg = e.getErrCodeDes();
            log.error("微信付款成功或失败回调失败：{}", errorMsg);
            throw new CustomException(errorMsg);
        }
    }


    public static NotifyResult getNotifyInfo(String data) {
        IOrderService iOrderService = PayContext.getIOrderService();
        return iOrderService.parseNotifyResult(data);
    }


    /**
     * 描述: 微信退款请求
     *
     * @param tradingFlow
     * @param refundNo
     * @param refundAmount
     * @return com.github.binarywang.wxpay.bean.result.WxPayRefundResult
     * @author zengzhangni
     * @date 2020/4/1 15:25
     */
    public static WxPayRefundResult wxPayRefund(String tradingFlow, String refundNo, BigDecimal refundAmount) {
        WxPayRefundResult wxPayRefundResult;
        try {
            // 微信退款
            // 元转成分
            int totalFee = yuanToFen(refundAmount);
            WxPayRefundRequest wxPayRefundRequest = WxPayRefundRequest.newBuilder().
                    outTradeNo(tradingFlow).
                    outRefundNo(refundNo).
                    totalFee(totalFee).
                    refundFee(totalFee).
                    build();
            WxPayService payService = PayContext.getWxPayService();
            wxPayRefundResult = payService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            log.error("退款异常:{}", e.getErrCodeDes());
            throw new CustomException(e.getErrCodeDes());
        }
        //验证返回信息
        verifyResult(wxPayRefundResult);

        return wxPayRefundResult;
    }

    /**
     * 描述: 通联退款请求
     *
     * @param order    订单信息
     * @param refundNo 订单退款单号
     * @return {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayRefundResult}
     * @author zengzhangni
     * @date 2020/4/1 15:25
     */
    public static AllinPayRefundResult allinPayRefund(Order order, String refundNo) {
        return allinPayRefund(order.getMemberId(), order.getTradingFlow(), refundNo, order.getPaidAmount());
    }

    public static AllinPayRefundResult allinPayRefund(RefundOrder order) {
        return allinPayRefund(order.getMemberId(), order.getTradingFlow(), order.getRefundNumber(), order.getRefundAmount());
    }

    public static AllinPayRefundResult allinPayRefund(Long userId, String tradingFlow, String refundNo, BigDecimal refundAmount) {
        AllinPayRefundResult result;
        try {
            // 通联退款
            // 元转成分
            Long totalFee = yuanToFenL(refundAmount);

            AllinPayRefundParams params = AllinPayRefundParams.builder()
                    .bizOrderNo(refundNo)
                    .oriBizOrderNo(tradingFlow)
                    .bizUserId(userId.toString())
                    .refundType(OrderRefundEnums.D0.getCode())
                    .amount(totalFee)
                    .build();

            IOrderService orderService = PayContext.getIOrderService();
            result = orderService.refund(params);
        } catch (Exception e) {
            log.error("通联退款异常.", e);
            throw new CustomException("通联退款异常.");
        }
        return result;
    }


    /**
     * 描述:微信订单查询结果
     *
     * @param tradingFlow
     * @return com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult
     * @author zengzhangni
     * @date 2020/4/10 16:54
     */
    public static WxPayOrderQueryResult wxQueryOrder(String tradingFlow) {
        WxPayOrderQueryResult result;
        try {
            WxPayService payService = PayContext.getWxPayService();
            WxPayOrderQueryRequest queryRequest = WxPayOrderQueryRequest.newBuilder().
                    outTradeNo(tradingFlow).build();
            result = payService.queryOrder(queryRequest);
        } catch (WxPayException e) {
            log.error("订单查询异常:{}", e.getErrCodeDes());
            throw new CustomException(e.getErrCodeDes());
        }

        //验证返回信息
        verifyResult(result);

        return result;
    }

    /**
     * 通联订单查询结果
     *
     * @param orderNumber 订单编号
     * @author: GongJunZheng
     * @date: 2020/9/28 14:49
     * @return: {@link com.meiyuan.catering.allinpay.model.result.order.AllinPayOrderDetailResult}
     * @version V1.5.0
     **/
    public static AllinPayOrderDetailResult allinQueryOrder(String orderNumber) {
        AllinPayOrderDetailResult result;
        try {
            IOrderService orderService = PayContext.getIOrderService();
            AllinPayOrderDetailParams params = AllinPayOrderDetailParams.builder()
                    .bizOrderNo(orderNumber)
                    .build();
            result = orderService.orderDetail(params);
        } catch (Exception e) {
            log.error("通联订单查询异常。", e);
            throw new CustomException("通联订单查询异常。");
        }
        return result;
    }

    public static Boolean isHaveWxPay(AllinPayOrderDetailResult result) {
        try {
            return Objects.equals(result.getOrderStatus(), OrderStatusEnums.TRANSACTION_SUCC.getStatus());
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 描述:验证结果信息
     *
     * @param result
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:50
     */
    private static void verifyResult(BaseWxPayResult result) {
        if (!Objects.equals(result.getReturnCode(), SUCCESS)) {
            log.error("结果异常:{} ", result.getErrCodeDes());
            throw new CustomException(result.getErrCodeDes());
        }
        if (!Objects.equals(result.getResultCode(), SUCCESS)) {
            log.error("结果异常:{} ", result.getErrCodeDes());
            throw new CustomException(result.getErrCodeDes());
        }
    }


    /**
     * 描述:元转分
     *
     * @param amount
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/4/9 13:33
     */
    public static Integer yuanToFen(BigDecimal amount) {
        return amount.multiply(B_100).intValue();
    }

    public static Long yuanToFenL(BigDecimal amount) {
        return amount.multiply(B_100).longValue();
    }

    /**
     * 描述:分转元
     *
     * @param fen
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/4/9 13:34
     */
    public static BigDecimal fenToYuan(Integer fen) {
        String fee = BaseWxPayResult.fenToYuan(fen);
        return new BigDecimal(fee);
    }

    public static BigDecimal fenToYuan(String fen) {
        return fenToYuan(Integer.valueOf(fen));
    }

    public static String getNotifyUrl(BaseAllinPayService service, NotifyEnum notifyEnum) {
        return getNotifyUrl(service.getConfig(), notifyEnum);
    }

    public static String getNotifyUrl(AllinPayConfig config, NotifyEnum notifyEnum) {
        return getNotifyUrl(config.getDomain(), notifyEnum);
    }

    public static String getNotifyUrl(String domain, NotifyEnum notifyEnum) {
        return getNotifyUrl(domain, notifyEnum.getNotifyUrl());
    }

    public static String getNotifyUrl(String domain, String notifyUrl) {
        return domain + notifyUrl;
    }

    public static String getAccountSetNo(BaseAllinPayService service) {
        return getAccountSetNo(service.getConfig());
    }

    public static String getAccountSetNo(AllinPayConfig config) {
        return config.getAccountSetNo();
    }

    public static String getPayVspCusId(BaseAllinPayService service) {
        return getPayVspCusId(service.getConfig());
    }

    public static String getPayVspCusId(AllinPayConfig config) {
        return config.getVspCusId();
    }

    public static String getSubsidyAccountNo(BaseAllinPayService service) {
        return getSubsidyAccountNo(service.getConfig());
    }

    public static String getSubsidyAccountNo(AllinPayConfig config) {
        return config.getSubsidyAccountNo();
    }

    public static String getSplitAccountNo(BaseAllinPayService service) {
        return getSplitAccountNo(service.getConfig());
    }

    public static String getSplitAccountNo(AllinPayConfig config) {
        return config.getSplitAccountNo();
    }


    public static String getWxAppId(IOrderService iOrderService) {
        return getWxAppId(iOrderService.getConfig());
    }

    public static String getWxAppId(AllinPayConfig config) {
        return config.getWxAppId();
    }

    /**
     * 描述:代付订单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/10/10 11:57
     * @since v1.5.0
     */
    public static String getAgentPayOrderNo() {
        return CodeGenerator.agentPayOrderNo();
    }

    /**
     * 描述:代付批次号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/10/10 11:57
     * @since v1.5.0
     */
    public static String getAgentPayBatchNo() {
        return CodeGenerator.agentPayBatchNo();
    }

    /**
     * 描述:转账订单号
     *
     * @param
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/10/10 11:57
     * @since v1.5.0
     */
    public static String getTransferNo() {
        return CodeGenerator.getTransferNo();
    }

    /**
     * 判断商家是否已完成结算信息
     *
     * @param shopId 商家ID
     * @author: GongJunZheng
     * @date: 2020/10/10 14:26
     * @return: {@link Boolean}
     * @version V1.5.0
     **/
    public static Boolean shopSignStatus(Long shopId) {
        MerchantUtils merchantUtils = PayContext.getMerchantUtils();
        ShopInfoDTO shop = merchantUtils.getShop(shopId);


        log.info("shop:{}", shop);

        //门店信息为null 或者 审核状态不是完成
        if (null == shop || !TlAuditStatusEnum.FINISH.getStatus().equals(shop.getAuditStatus())) {
            // 查询数据库
            ShopBankClient shopBankClient = PayContext.getShopBankClient();
            Result<ShopBankInfoVo> shopBankInfoResult = shopBankClient.getShopBankInfo(shopId);
            if (shopBankInfoResult.failure()) {
                return Boolean.FALSE;
            } else {
                ShopBankInfoVo bankInfo = shopBankInfoResult.getData();
                log.info("bankInfo:{}", bankInfo);
                return TlAuditStatusEnum.FINISH.getStatus().equals(bankInfo.getAuditStatus());
            }
        } else {
            return TlAuditStatusEnum.FINISH.getStatus().equals(shop.getAuditStatus());
        }
    }


    public static JSONObject builderPayMethod(PayMethodKeyEnums payMethodEnum, IOrderService orderService, String openId, Integer amount) {

        if (PayMethodKeyEnums.SCAN_WEIXIN_ORG.equals(payMethodEnum)) {
            //集团扫码支付
            return builderScanPayMethod(orderService, openId, amount);

        } else if (PayMethodKeyEnums.WECHATPAY_MINIPROGRAM_ORG.equals(payMethodEnum)) {
            //集团小程序支付
            return builderMiniPayMethod(orderService, openId, amount);
        }

        throw new CustomException(payMethodEnum.getName() + "支付方式不支持");
    }

    public static JSONObject builderMiniPayMethod(IOrderService orderService, String openId, Integer amount) {

        String vspCusId = PayUtil.getPayVspCusId(orderService);
        String wxAppId = PayUtil.getWxAppId(orderService);

        WechatPayMiniOrg build = WechatPayMiniOrg.builder()
                .vspCusid(vspCusId)
                .subAppid(wxAppId)
                .acct(openId)
                .amount(amount)
                .build();

        return build.toJsonObject();
    }

    public static JSONObject builderScanPayMethod(IOrderService orderService, String openId, Integer amount) {

        String vspCusId = PayUtil.getPayVspCusId(orderService);

        ScanWechatOrg build = ScanWechatOrg.builder()
                .vspCusid(vspCusId)
                .amount(amount)
                .build();

        return build.toJsonObject();
    }


    public static Object builderPayResult(PayMethodKeyEnums payMethodEnum, AllinPayAgentCollectApplyResult result) {
        log.info("result:{}", result);
        if (PayMethodKeyEnums.SCAN_WEIXIN_ORG.equals(payMethodEnum)) {
            return result;
        } else if (PayMethodKeyEnums.WECHATPAY_MINIPROGRAM_ORG.equals(payMethodEnum)) {
            return JSON.parseObject(result.getPayInfo(), WxMiniPayResult.class);
        }
        return null;
    }
}
