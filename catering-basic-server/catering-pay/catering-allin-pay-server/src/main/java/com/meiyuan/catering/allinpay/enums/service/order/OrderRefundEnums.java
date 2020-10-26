package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/09/25 16:09
 * @description 订单退款方式
 **/

public enum OrderRefundEnums {

    D1("D1", "D+1 14:30向渠道发起退款"),
    D0("D0", "D+0实时向渠道发起退款");

    private String code;
    private String desc;

    OrderRefundEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
