package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 结算订单类型：1：购物车订单，2：团购订单
 *
 * @author XiJie-Xie
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum OrderCalculateTypeEnum {

    /**
     * 订单关闭操作类型枚举定义
     */
    SHOPPING_CART(1, "购物车"),

    BULK(2, "团购");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderCalculateTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
