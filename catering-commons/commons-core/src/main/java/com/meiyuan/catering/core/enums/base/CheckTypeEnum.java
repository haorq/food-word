package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 敏感信息校验类型枚举
 * @Date  2020/3/10 0010 14:22
 */
@Getter
public enum CheckTypeEnum {
    /**
     * 文字
     **/
    TEXT(Values.TEXT, "文字"),
    /**
     * 图片
     **/
    PHOTO(Values.PHOTO, "图片");
    private Integer status;
    private String desc;
    CheckTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CheckTypeEnum parse(int status) {
        for (CheckTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer PHOTO = 2;
        private static final Integer TEXT = 1;
    }
}
