package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 状态枚举
 * @Date  2020/3/10 0010 14:22
 */
@Getter
public enum StatusEnum {
    /**
     * 启用
     **/
    ENABLE(Values.ENABLE, "启用"),
    /**
     * 禁用
     **/
    ENABLE_NOT(Values.ENABLE_NOT, "禁用");
    private Integer status;
    private String desc;
    StatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static StatusEnum parse(int status) {
        for (StatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer ENABLE_NOT = 2;
        private static final Integer ENABLE = 1;
    }
}
