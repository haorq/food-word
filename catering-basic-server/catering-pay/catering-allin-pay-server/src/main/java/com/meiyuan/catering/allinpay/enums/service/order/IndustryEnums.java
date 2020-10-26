package com.meiyuan.catering.allinpay.enums.service.order;

/**
 * created on 2020/8/17 16:08
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum IndustryEnums {
    /**  */
    E_COMMERCE_PLATFORM("1918","团购"),
    CATERING_PLATFORM("2311","食品饮料"),
    ;
    private String code;
    private String name;
    IndustryEnums(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
