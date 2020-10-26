package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author lhm
 * @date 2020/4/8 15:51
 **/
@Getter
public enum IsDefaultEnum {

    /**
     * 默认
     **/
    DEFAULT(IsDefaultEnum.Values.DEFAULT, "默认"),
    /**
     * 不默认
     **/
    NO_DEFAULT(IsDefaultEnum.Values.NO_DEFAULT, "不默认");
    private Integer status;
    private String desc;
    IsDefaultEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static IsDefaultEnum parse(int status) {
        for (IsDefaultEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer NO_DEFAULT = 2;
        private static final Integer DEFAULT = 1;
    }

}
