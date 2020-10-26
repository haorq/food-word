package com.meiyuan.catering.allinpay.enums.service;

/**
 * created on 2020/8/14 15:08
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum DeviceTypeEnums {
    /**
     *
     */
    MOBILE(1L, "MOBILE"),
    PC(2L, "PC");
    private Long type;
    private String desc;

    DeviceTypeEnums(Long type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Long getType() {
        return type;
    }
}
