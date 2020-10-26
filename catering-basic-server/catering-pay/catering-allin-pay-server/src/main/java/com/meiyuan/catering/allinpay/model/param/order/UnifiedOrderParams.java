package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.enums.service.order.PayMethodEnums;
import com.meiyuan.catering.allinpay.model.bean.order.ProfitReceiver;
import com.meiyuan.catering.allinpay.model.bean.paymethod.WithdrawPay;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * created on 2020/8/21 10:21
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class UnifiedOrderParams {
    /** 唯一用户标识 微信是openid 通联是业务系统(用户/商户)唯一标识 */
    private String uniqueIdentity;
    /** 支付订单号 */
    private String payOrderNo;
    /** 订单总金额 分 */
    private Integer totalFee;
    /** 手续费 */
    private Integer fee;
    /** 微信小程序支付appid参数 */
    private String subAppid;
    /** 支付授权码，支付宝被扫刷卡支付时,用户的付款二维码 */
    private String authcode;

    /** 过期时间  yyyy-MM-dd HH:mm:ss */
    private String orderExpireDatetime;

    /** 交易类型 */
    private PayMethodEnums tradeType;

    /** 通知地址 */
    private String notifyUrl;
    /** 商品描述 20 */
    private String body;
    /** 终端IP */
    private String spbillCreateIp;

    /** 分账标识 */
    private Boolean profitSharing;
    /** 分账者 */
    private List<ProfitReceiver> profitReceivers;
    /** 通联通代付参数 */
    private WithdrawPay withdrawPay;
}
