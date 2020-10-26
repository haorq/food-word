package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * created on 2020/8/17 15:55
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum ValidateTypeEnums {
    /** */
    NO_VALIDATE(0L,"仅渠道验证，通商云不做交易验证"),
    SMS_VALIDATE(1L,"通商云发送并验证短信验证码，有 效期3分钟"),
    PASSWORD_VALIDATE(2L,"验证通商云支付密码"),
    ;
    private Long type;
    private String desc;
    ValidateTypeEnums(Long type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public Long getType() {
        return type;
    }
}
