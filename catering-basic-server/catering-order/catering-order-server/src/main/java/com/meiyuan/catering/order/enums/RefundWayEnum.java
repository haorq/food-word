package com.meiyuan.catering.order.enums;

/**
 * 退款方式
 * @author lh
 */

public enum RefundWayEnum {

    /**
     * 暂时只有微信
     */
    ORIGINAL_WAY(1,"微信");


    private Integer code;
    private String message;

    RefundWayEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String parse(Integer code){
        for (RefundWayEnum value : values()) {
            if (code.compareTo(value.code)==0){
                return value.message;
            }
        }
        return "未知退款方式";
    }
}
