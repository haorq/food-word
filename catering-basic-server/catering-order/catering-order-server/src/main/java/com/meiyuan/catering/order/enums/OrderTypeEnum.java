package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author zengzhangni
 * @date 2019/3/31
 */
@Getter
public enum OrderTypeEnum {
    /**
     * 1：普通订单，2：团购订单，3：拼单订单，4：菜单订单
     */
    COMMON(1, "普通订单"),
    BULK(2, "团购订单"),
    SHARE_BILL(3, "拼单订单"),
    MENU(4, "菜单订单");

    private final Integer status;

    private final String desc;

    OrderTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
