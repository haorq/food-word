package com.meiyuan.catering.finance.enums;

import lombok.Getter;

/**
 * @author lhm
 * 充值订单状态
 * @date 2020/4/7 14:56
 **/
@Getter
public enum  RechargeOrderStatusEnum {

    /**
     * 待支付
     */
    WAIT_PAY(1, "待支付"),
    /**
     * 支付成功
     */
    PAY_SUCCESS(2, "支付成功"),
    /**
     * 取消
     */
    CANCLE(3, "取消");

    private Integer status;
    private String desc;

    RechargeOrderStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
