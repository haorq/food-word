package com.meiyuan.catering.order.enums;


/**
 * @author xie-xi-jie
 * @date 2019/3/10
 * @description 订单来源，1：微信
 */
public enum OrderSourceEnum {
    /**
     * 描述: 订单来源（1：微信）
     */
    WX_CLIENT(1, "微信");

    private final Integer code;

    private final String desc;

    OrderSourceEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
