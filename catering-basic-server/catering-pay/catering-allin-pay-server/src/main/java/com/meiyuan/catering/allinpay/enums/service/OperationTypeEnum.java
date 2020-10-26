package com.meiyuan.catering.allinpay.enums.service;

import lombok.Getter;

/**
 * @author zengzhangni
 * @date 2020/10/17 11:49
 * @since v1.5.0
 */
@Getter
public enum OperationTypeEnum {

    /**
     * 描述:操作
     */
    SET("set", "绑定"),
    QUERY("query", "绑定"),

    ;

    private String code;
    private String desc;


    OperationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
