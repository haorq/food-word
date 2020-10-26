package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/10 10:46
 * @description 删除枚举
 **/
@Getter
public enum DelEnum {
    /**
     * 没有删除
     **/
    NOT_DELETE(Values.NOT_DELETE, Boolean.FALSE,"没有删除"),
    /**
     * 已删除
     **/
    DELETE(Values.DELETE, Boolean.TRUE,"已删除");
    private Integer status;
    private Boolean flag;
    private String desc;
    DelEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.flag = flag;
        this.desc = desc;
    }

    public static DelEnum parse(int status) {
        for (DelEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer NOT_DELETE = 0;
        private static final Integer DELETE = 1;
    }
}
