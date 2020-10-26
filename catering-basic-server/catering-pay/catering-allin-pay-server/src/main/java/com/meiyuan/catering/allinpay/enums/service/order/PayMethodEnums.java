package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * created on 2020/8/21 10:48
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum PayMethodEnums {
    /**
     * 微信小程序支付
     */
    MINIPROGRAM,
    /**
     * 微信公众号支付
     */
    WECHATPAY_PUBLIC,
    /**
     * 微信H5支付
     */
    WECHATPAY_H5,
    /**
     * 微信APP支付
     */
    WECHATPAY_APP,
    /**
     * 微信扫码(正扫) 用户扫商户二维码
     */
    WECHATPAY_NATIVE,
    /**
     * 微信扫码(正扫) 集团模式
     */
    WECHATPAY_NATIVE_ORG,
    /**
     * 付款码支付(被扫) 商户扫用户二维码 (第三方一般聚合，微信原生只可扫微信二维码，支付宝原生可扫支付宝二维码)
     */
    MICROPAY,
    /**
     * 通联通代付
     */
    WITHDRAW_TLT;
}
