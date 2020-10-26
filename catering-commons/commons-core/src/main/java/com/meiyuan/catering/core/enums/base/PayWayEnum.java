package com.meiyuan.catering.core.enums.base;


import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * 支付方式
 *
 * @author zengzhangni
 * @date 2019/3/23
 */
@Getter
public enum PayWayEnum {
    /**
     * 0元支付
     */
    ZERO_PAY(0, "0元支付"),
    /**
     * 余额支付
     */
    BALANCE(1, "余额支付"),
    /**
     * 微信支付
     */
    WX(2, "微信支付"),
    /**
     * 微信支付(通联)
     */
    WX_ALLIN(3, "微信支付(通联)"),
    ;


    private final Integer payWay;
    private final String desc;

    public static PayWayEnum parse(int status) {
        for (PayWayEnum type : values()) {
            if (type.payWay == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    PayWayEnum(Integer payWay, String desc) {
        this.payWay = payWay;
        this.desc = desc;
    }
}
