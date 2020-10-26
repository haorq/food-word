package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/09/27 16:09
 * @description 提现方式枚举
 **/

public enum WithdrawTypeEnums {

    D0("D0", "D+0到账"),
    D1("D1", "D+1到账"),
    T1_CUSTOMIZED("T1customized", "T+1到账，仅工作日代付"),
    D0_CUSTOMIZED("D0customized", "D+0到账，根据平台资金头寸付款");

    private String code;
    private String desc;

    WithdrawTypeEnums(String code, String desc) {
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
