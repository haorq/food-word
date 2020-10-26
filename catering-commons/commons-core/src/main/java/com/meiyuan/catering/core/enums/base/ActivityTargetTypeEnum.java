package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：活动对象类型
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/21 10:30
 */
@Getter
public enum ActivityTargetTypeEnum {
    /**
     * 用户
     **/
    USER_TYPE(Values.USER_TYPE, "用户"),
    /**
     * 品牌
     **/
    BRAND_TYPE(Values.BRAND_TYPE, "品牌");

    private Integer status;
    private String desc;
    ActivityTargetTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ActivityTargetTypeEnum parse(int status) {
        for (ActivityTargetTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer USER_TYPE = 1;
        private static final Integer BRAND_TYPE = 2;
    }
}
