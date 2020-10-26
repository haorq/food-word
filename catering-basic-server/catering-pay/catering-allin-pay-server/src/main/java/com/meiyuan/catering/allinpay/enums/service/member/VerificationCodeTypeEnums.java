package com.meiyuan.catering.allinpay.enums.service.member;

import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/9/24 17:07
 * @since v1.1.0
 */
@Getter
public enum VerificationCodeTypeEnums {

    /**
     * 验证码类型 9-绑定手机 6-解绑手机
     */
    UNBIND(6L, "解绑手机"),

    BIND(9L, "绑定手机");

    private Long type;
    private String desc;

    VerificationCodeTypeEnums(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
