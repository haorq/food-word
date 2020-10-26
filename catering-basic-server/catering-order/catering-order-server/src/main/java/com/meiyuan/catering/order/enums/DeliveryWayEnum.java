package com.meiyuan.catering.order.enums;


/**
 * @author xie-xi-jie
 * @date 2019/3/10
 * @description 配送方式
 */
public enum DeliveryWayEnum {
    /**
     * 取餐方式（1：外卖配送，2：到店自取）
     */
    Delivery(1, "配送"),
    invite(2, "自取");

    private final Integer code;

    private final String desc;

    DeliveryWayEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String parse(Integer code) {

        for (DeliveryWayEnum item : values()) {
            if (code.compareTo(item.getCode()) == 0) {
                return item.desc;
            }
        }
        return "未知取餐方式";
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
