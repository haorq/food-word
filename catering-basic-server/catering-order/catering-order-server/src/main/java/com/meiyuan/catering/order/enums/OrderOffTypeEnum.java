package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单关闭/取消操作类型枚举定义
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum OrderOffTypeEnum {

    /**
     * 订单关闭操作类型枚举定义
     */
    MEMBER_CANCEL(1, "客户"),

    EMPLOYEE_CANCEL(2, "商家"),

    AUTO_OFF(3, "系统");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderOffTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
