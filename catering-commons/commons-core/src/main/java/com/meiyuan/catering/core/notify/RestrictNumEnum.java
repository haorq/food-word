package com.meiyuan.catering.core.notify;

import lombok.Getter;

/**
 * 短信次数限制数量枚举
 *
 * @author zengzhangni
 * @date 2020/4/8
 */
@Getter
public enum RestrictNumEnum {

    /**
     * 设置/重置密码发送验证码
     */
    RESET_PAY_PWD(3),

    /**
     * 小程序商户申请验证码次数
     */
    SHOP_APPLY(3);

    /**
     * 限制次数
     */
    private Integer num;


    RestrictNumEnum(Integer num) {
        this.num = num;
    }
}
