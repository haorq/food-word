package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/21 12:51
 * @description 简单描述
 **/
@Getter
public enum FixedOrAllEnum {
    /**
     * 没有推送
     **/
    NO_PUSH(FixedOrAllEnum.Values.NO_PUSH, "没有推送"),
    /**
     * 所有商家
     **/
    ALL(FixedOrAllEnum.Values.ALL, "所有商家"),
    /**
     * 指定商家
     **/
    FIXED(FixedOrAllEnum.Values.FIXED, "指定商家");
    private Integer status;
    private String desc;
    FixedOrAllEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static FixedOrAllEnum parse(int status) {
        for (FixedOrAllEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }
    private static class  Values{
        private static final Integer NO_PUSH = -1;
        private static final Integer ALL = 1;
        private static final Integer FIXED = 2;
    }
}
