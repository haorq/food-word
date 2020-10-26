package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单活动类型枚举定义 1：秒杀；2：团购；3：拼单；4：特价商品
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum OrderActivityEnum {

    /**
     * 订单活动类型枚举定义
     */
    SECONDS_KILL(1, "秒杀"),

    BULK(2, "团购"),

    SHARE_BILL(3, "拼单"),

    SPECIAL(4, "特价商品");

    /**
     * 枚举code
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderActivityEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
