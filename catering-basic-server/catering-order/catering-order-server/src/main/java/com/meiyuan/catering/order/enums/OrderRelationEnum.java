package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单关联维度枚举关联维度（1：订单维度关联；2：订单商品维度关联）
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum OrderRelationEnum {

    /**
     * 订单关闭操作类型枚举定义
     */
    ORDER(1, "订单"),

    GOODS(2, "商品");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderRelationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
