package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 结算类型:1--菜单，2--商品/秒杀，3--拼单，4——团购
 *
 * @author XiJie-Xie
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum CalculateTypeEnum {

    /**
     * 订单结算类型枚举定义
     */

    MENU(1, "菜单"),

    ORDINARY(2, "普通、秒杀"),

    SHARE_BILL(3, "拼单"),

    BULK(4, "团购");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    CalculateTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
