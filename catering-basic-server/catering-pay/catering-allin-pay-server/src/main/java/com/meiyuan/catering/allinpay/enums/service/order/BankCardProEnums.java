package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * @author GongJunZheng
 * @date 2020/09/27 16:09
 * @description 通联银行卡/账户属性枚举
 **/

public enum BankCardProEnums {

    PERSONAL_CARD(0L, "个人银行卡"),
    COMPANY_PUBLIC_ACCOUNT(1L, "企业对公账户");

    private Long code;
    private String desc;

    BankCardProEnums(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
