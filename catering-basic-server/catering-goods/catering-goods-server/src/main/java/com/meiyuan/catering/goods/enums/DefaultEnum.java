package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/19 10:55
 * @description 简单描述
 **/
@Getter
public enum DefaultEnum {
    /**
     * 默认
     **/
    DEFAULT(Values.DEFAULT, "默认"),
    /**
     * 新增
     **/
    ADD(Values.ADD, "新增");
    private Integer status;
    private String desc;
    DefaultEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static DefaultEnum parse(int status) {
        for (DefaultEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer ADD = 1;
        private static final Integer DEFAULT = 2;
    }
}
