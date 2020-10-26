package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @author XXJ
 * @date 2020/3/10 10:46
 * @description 分段锁 分组类型
 **/
@Getter
public enum  CateringLockEnum {
    /** 分段锁 分组类型 */
    /**
     * 锁 类型
     */
    ORDER("ORDER", "订单"),

    USER("USER", "用户"),

    GOODS("GOODS", "商品"),

    SECONDS_KILL("GOODS", "秒杀");

    /**
     * 枚举group
     */
    private final String group;

    /**
     * 枚举描述
     */
    private final String desc;

    CateringLockEnum(String group, String desc) {
        this.group = group;
        this.desc = desc;
    }
}
