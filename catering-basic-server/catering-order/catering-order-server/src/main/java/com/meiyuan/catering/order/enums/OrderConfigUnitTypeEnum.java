package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 订单配置单位类型（year：年，month：月，day：天，hour：时，minute：分，second：秒）
 *
 * @author xxj
 * @date 2019/3/20
 */
@Getter
public enum OrderConfigUnitTypeEnum {
    /**
     * 订单关闭操作类型枚举定义
     */
    YEAR("year", "年"),

    MONTH("month", "月"),

    DAY("day", "天"),

    HOUR("hour", "自动结束交易，时"),

    MINUTE("minute", "分"),

    SECOND("second", "秒");

    /**
     * 枚举code
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderConfigUnitTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
