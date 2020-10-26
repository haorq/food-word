package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：小程序类目状态枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 10:09
 */
@Getter
public enum WxCategoryStatusEnum {
    /**
     *启用
     **/
    ENABLE(Values.ENABLE, Boolean.TRUE, "启用"),
    /**
     *禁用
     **/
    DISABLE(Values.DISABLE, Boolean.FALSE,"禁用");

    private Integer status;
    private String desc;
    private Boolean flag;
    WxCategoryStatusEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.desc = desc;
        this.flag = flag;
    }

    public static WxCategoryStatusEnum parse(int status) {
        for (WxCategoryStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer ENABLE = 1;
        private static final Integer DISABLE = 0;
    }
}
