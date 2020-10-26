package com.meiyuan.catering.order.enums;

/**
 * @author GongJunZheng
 * @date 2020/10/14 09:10
 * @description 订单分账收款人类型
 **/

public enum CollectionUserTypeEnum {

    SHOP(1, "商户"),
    PLATFORM(2, "平台"),
    ;

    private Integer type;
    private String desc;

    CollectionUserTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
