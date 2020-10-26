package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/10/13 14:10
 * @description 订单分账类型
 **/
@Getter
public enum OrderSplitBillTypeEnum {

    ORDER(1, "商家可分订单金额"),
    DELIVERY(2, "订单配送费"),
    DEBT(3, "商家负债分账");

    private Integer type;
    private String desc;

    OrderSplitBillTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
