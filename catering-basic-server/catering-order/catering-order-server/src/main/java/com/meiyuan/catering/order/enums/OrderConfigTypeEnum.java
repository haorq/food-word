package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 订单配置类型
 *
 * @author xxj
 * @date 2019/3/20
 */
@Getter
public enum OrderConfigTypeEnum {
    /**
     * 订单关闭操作类型枚举定义
     */
    NORMAL("normal", "未付款，订单自动关闭"),

    COMPLETE("complete", "自动结束交易，不能申请售后"),

    COMPLETE_APPRAISE("completeAppraise", "自动五星好评");

    /**
     * 枚举code
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderConfigTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
