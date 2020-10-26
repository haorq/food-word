package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 交易状态枚举
 *
 * @author zengzhangni
 * @date 2019/4/1
 */
@Getter
public enum TradeStatusEnum {
    /**
     * 0：未交易；1：交易中；2：交易成功；3：交易失败）
     */
    NOT(0, "未交易"),
    TRADE(1, "交易中"),
    SUCCESS(2, "交易成功"),
    FAIL(3, "交易失败"),
    WAITING(4, "待交易(门店结算信息未完善/其他[配送费补贴])"),
    CLOSE(5, "交易关闭"),
    ;

    private final Integer status;

    private final String desc;

    TradeStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
