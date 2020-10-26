package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * description：用户等级类型枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 16:56
 */
@Getter
public enum UserLevelEnum {

    /**
     * 普通用户
     **/
    ORDINARY(Values.ORDINARY, "普通用户"),
    /**
     * 会员卡用户
     **/
    MEMBER(Values.MEMBER, "会员卡用户"),

    /**
     * 其他
     **/
    OTHER(Values.OTHER, "其他");



    private Integer status;
    private String desc;

    UserLevelEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserLevelEnum parse(int status) {
        for (UserLevelEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer ORDINARY = 1;
        private static final Integer MEMBER = 2;
        private static final Integer OTHER = 3;
    }
}
