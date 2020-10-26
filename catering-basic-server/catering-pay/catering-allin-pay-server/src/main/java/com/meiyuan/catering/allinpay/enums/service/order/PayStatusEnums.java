package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/10/10 09:10
 * @description 通联支付状态枚举
 **/

public enum PayStatusEnums {

    PENDING("pending", "进行中"),
    OK("OK", "成功"),
    SUCCESS("success", "成功"),
    FAIL("fail", "失败"),
    ERROR("ERROR", "失败");

    private String code;
    private String desc;

    PayStatusEnums(String code, String desc) {
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
