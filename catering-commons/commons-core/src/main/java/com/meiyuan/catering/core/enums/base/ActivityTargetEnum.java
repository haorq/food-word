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
public enum ActivityTargetEnum {

    /**
     * 企业用户
     **/
    ENTERPRISE_USER(Values.ENTERPRISE_USER, "企业用户"),
    /**
     * 个人用户
     **/
    PERSONAL_USER(Values.PERSONAL_USER, "个人用户"),
    /**
     * 所有用户
     */
    ALL_USER(Values.ALL_USER, "所有用户"),
    /**
     * 非自营品牌
     **/
    ENTERPRISE_BRAND(Values.ENTERPRISE_USER, "非自营品牌"),
    /**
     * 自营品牌
     **/
    PERSONAL_BRAND(Values.PERSONAL_USER, "自营品牌"),
    /**
     * 所有品牌
     */
    ALL_BRAND(Values.ALL_USER, "所有品牌");

    private Integer status;
    private String desc;

    ActivityTargetEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ActivityTargetEnum parse(int status) {
        for (ActivityTargetEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class Values {
        private static final Integer ENTERPRISE_USER = 2;
        private static final Integer PERSONAL_USER = 1;
        private static final Integer ALL_USER = 0;
    }
}
