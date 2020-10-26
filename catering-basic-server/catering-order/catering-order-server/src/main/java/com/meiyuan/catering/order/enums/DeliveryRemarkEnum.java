package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/10/14 17:10
 * @since v1.5.0
 */
@Getter
public enum DeliveryRemarkEnum {

    /**
     * 描述: 配送信息备注
     */
    DELIVERY(1, "骑手接单,扣取平台配送费", "平台收取配送费"),
    AGAIN_DELIVERY(2, "再次配送,扣取平台配送费", "平台收取配送费"),
    REFUND(3, "订单退款,删除内扣信息,回滚负债信息", "订单退款,回滚负债信息"),
    CANCEL_SHOP(4, "商家原因取消配送,不退回配送费", null),
    CANCEL_RIDER(5, "骑手原因取消配送,退回配送费", "骑手原因取消配送,扣减负债配送费"),
    CANCEL_WITHOUT_ACCEPT_ORDER(6, "骑手未接单，商家取消订单,退回配送费", "商家取消订单,回滚负债信息"),
    ;


    private Integer code;
    private String desc;
    private String remark;

    DeliveryRemarkEnum(int code, String desc, String remark) {
        this.code = code;
        this.desc = desc;
        this.remark = remark;
    }
}
