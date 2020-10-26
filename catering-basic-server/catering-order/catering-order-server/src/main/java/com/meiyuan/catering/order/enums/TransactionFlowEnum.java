package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 交易流水类型枚举
 *
 * @author zengzhangni
 * @date 2019/4/1
 */
@Getter
public enum TransactionFlowEnum {
    /**
     * 1：订单支付收入；2：订单退款支出
     */
    PAY(1, "订单支付收入"),
    REFUND(2, "订单退款支出");

    private final Integer status;

    private final String desc;

    TransactionFlowEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
