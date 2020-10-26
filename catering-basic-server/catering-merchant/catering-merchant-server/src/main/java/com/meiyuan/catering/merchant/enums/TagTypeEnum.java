package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 标签类型：1：默认标签 2：系统添加标签
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum TagTypeEnum {
    /**
     * 默认标签
     */
    DEFAULT(Values.DEFAULT, "默认标签"),
    /**
     * 系统添加标签
     */
    SYSTEM(Values.SYSTEM, "系统添加标签");

    private Integer status;
    private String desc;
    TagTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static TagTypeEnum parse(int status) {
        for (TagTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer DEFAULT = 1;
        private static final Integer SYSTEM = 2;
    }
}
