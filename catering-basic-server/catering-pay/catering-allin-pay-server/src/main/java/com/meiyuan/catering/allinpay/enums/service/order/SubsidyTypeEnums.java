package com.meiyuan.catering.allinpay.enums.service.order;

import com.meiyuan.catering.core.exception.CustomException;

import java.util.Objects;

/**
 * @author GongJunZheng
 * @date 2020/10/10 14:10
 * @description 补贴分账类型
 **/

public enum  SubsidyTypeEnums {

    TICKET(1, "优惠券"),
    DELIVERY(2, "配送"),
    ;
    private Integer type;
    private String desc;

    SubsidyTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String parse(Integer type) {
        for (SubsidyTypeEnums value : values()) {
            if (Objects.equals(value.type, type)) {
                return value.desc;
            }
        }
        throw new CustomException("非法type");
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
