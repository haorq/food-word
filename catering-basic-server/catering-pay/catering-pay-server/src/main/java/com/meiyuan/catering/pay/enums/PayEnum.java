package com.meiyuan.catering.pay.enums;


import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * 支付方式
 *
 * @author zengzhangni
 * @date 2019/4/2
 */
@Getter
public enum PayEnum {
    /**
     * 0元支付
     */
    ZERO_PAY(0, "0元支付", "myZeroPayServiceImpl"),
    /**
     * 余额支付
     */
    BALANCE(1, "余额支付", "myBalancePayServiceImpl"),
    /**
     * 微信支付
     */
    WX(2, "微信支付", "myWxPayServiceImpl"),
    /**
     * 微信支付(通联)
     */
    WX_ALLIN(3, "微信支付(通联)", "myAllinPayServiceImpl"),
    ;

    private final Integer payWay;
    private final String desc;
    private final String impl;

    public static PayEnum parse(int status) {
        for (PayEnum type : values()) {
            if (type.payWay == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }


    PayEnum(Integer payWay, String desc, String impl) {
        this.payWay = payWay;
        this.desc = desc;
        this.impl = impl;
    }
}
