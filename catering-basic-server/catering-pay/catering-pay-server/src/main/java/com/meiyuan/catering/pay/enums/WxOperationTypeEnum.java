package com.meiyuan.catering.pay.enums;


import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * 订单操作类型（1：支付，2：取消）
 *
 * @author zengzhangni
 * @date 2019/4/2
 */
@Getter
public enum WxOperationTypeEnum {
    /**
     * 余额支付
     */
    PAY(1, "支付"),
    /**
     * 微信支付
     */
    CANCEL(2, "取消");

    private final Integer code;
    private final String desc;

    public static WxOperationTypeEnum parse(int code) {
        for (WxOperationTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }


    WxOperationTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
