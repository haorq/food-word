package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/09/25 14:09
 * @description 支付方式key枚举
 **/

public enum PayMethodKeyEnums {

    GATEWAY_VSP("GATEWAY_VSP", "收银宝网关支付", "入金"),
    REALNAMEPAY("REALNAMEPAY", "实名付(单笔)", "入金"),
    REALNAMEPAY_BATCH("REALNAMEPAY_BATCH", "实名付(批量)", "入金"),
    QUICKPAY_TLT("QUICKPAY_TLT", "通联协议支付", "入金"),
    QUICKPAY_VSP("QUICKPAY_VSP", "收银宝快捷支付", "入金"),
    WECHATPAY_MINIPROGRAM("WECHATPAY_MINIPROGRAM", "微信小程序支付(收银宝)", "入金"),
    WECHATPAY_MINIPROGRAM_ORG("WECHATPAY_MINIPROGRAM_ORG", "微信小程序支付_集团(收银宝)", "入金"),
    WECHATPAY_APP_OPEN("WECHATPAY_APP_OPEN", "微信原生APP支付", "入金"),
    WECHATPAY_H5_OPEN("WECHATPAY_H5_OPEN", "微信原生H5支付", "入金"),
    CODEPAY_VSP("CODEPAY_VSP", "收银宝刷卡支付(被扫)--支持微信、支付宝、银联、手机QQ", "入金"),
    CODEPAY_VSP_ORG("CODEPAY_VSP_ORG", "收银宝刷卡支付(被扫)_集团--支持微信、支付宝、银联、手机QQ", "入金"),
    SCAN_WEIXIN("SCAN_WEIXIN", "微信扫码支付(正扫)--收银宝", "入金"),
    SCAN_WEIXIN_ORG("SCAN_WEIXIN_ORG", "微信扫码支付(正扫)_集团--收银宝", "入金"),
    WECHATPAY_SCAN_OPEN("WECHATPAY_SCAN_OPEN", "微信原生正扫支付", "入金"),
    SCAN_ALIPAY("SCAN_ALIPAY", "支付宝扫码支付(正扫)--收银宝", "入金"),
    SCAN_ALIPAY_ORG("SCAN_ALIPAY_ORG", "支付宝扫码支付(正扫)_集团--收银宝", "入金"),
    SCAN_UNIONPAY("SCAN_UNIONPAY", "银联扫码支付(正扫)--收银宝", "入金"),
    SCAN_UNIONPAY_ORG("SCAN_UNIONPAY_ORG", "银联扫码支付(正扫)_集团--收银宝", "入金"),
    WECHAT_PUBLIC("WECHAT_PUBLIC", "微信JS支付(公众号)--收银宝", "入金"),
    WECHAT_PUBLIC_ORG("WECHAT_PUBLIC_ORG", "微信JS支付(公众号)_集团--收银宝", "入金"),
    WECHATPAY_PUBLIB_OPEN("WECHATPAY_PUBLIB_OPEN", "微信原生JS支付", "入金"),
    ALIPAY_SERVICE("ALIPAY_SERVICE", "支付宝JS支付(生活号)--收银宝", "入金"),
    ALIPAY_SERVICE_ORG("ALIPAY_SERVICE_ORG", "支付宝JS支付_集团--收银宝", "入金"),
    QQ_WALLET("QQ_WALLET", "QQ钱包JS支付--收银宝", "入金"),
    QQ_WALLET_ORG("QQ_WALLET_ORG", "QQ钱包JS支付_集团--收银宝", "入金"),
    BALANCE("BALANCE", "账户余额", "账户内转账"),
    COUPON("COUPON", "代金券", "账户内转账"),
    WITHDRAW_TLT("WITHDRAW_TLT", "通联通代付", "出金"),
    ORDER_VSPPAY("ORDER_VSPPAY", "收银宝POS当面付及订单模式支付", "入金"),
    ALIPAY_APP_OPEN("ALIPAY_APP_OPEN", "支付宝原生支付APP支付", "入金");

    private String code;
    private String name;
    private String type;
    PayMethodKeyEnums(String code,String name,String type){
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}
