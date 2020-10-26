package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单优惠类型枚举定义
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 *
 */
@Getter
public enum OrderDiscountTypeEnum {

    /**
     * 订单优惠类型枚举定义 1：优惠卷）
     */
    TICKET(1, "优惠卷");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderDiscountTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
