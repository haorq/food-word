package com.meiyuan.catering.allinpay.enums.service;

import lombok.Getter;

/**
 * created on 2020/8/17 15:19
 *
 * @author yaozou
 * @since v1.0.0
 */
@Getter
public enum AcctTypeEnums {
    /**
     *
     */
    WECHAT_PUBLIC("weChatPublic", "微信公众号"),
    WECHAT_MINI_PROGRAM("weChatMiniProgram", "微信小程序"),
    ALIPAY_SERVICE("aliPayService", "支付宝生活号"),
    UNION_PAYJS("unionPayjs", "银联 JS");
    private String type;
    private String desc;

    AcctTypeEnums(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }


}
