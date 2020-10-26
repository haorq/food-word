package com.meiyuan.catering.order.enums;

/**
 * @author lh
 */

public enum DeliveryTypeEnum {
    self_delivery(1, "自配送"),
    dada_delivery(2, "达达配送"),
    ;


    private Integer code;
    private String message;

    DeliveryTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
