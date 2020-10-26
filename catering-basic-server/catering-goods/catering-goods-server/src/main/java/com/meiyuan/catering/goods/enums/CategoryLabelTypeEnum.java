package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/23 15:22
 * @description 简单描述
 **/
@Getter
public enum CategoryLabelTypeEnum {
    /**
     * 分类
     **/
    CATEGORY(Values.CATEGORY, "分类"),
    /**
     * 标签
     **/
    LABEL(Values.LABEL, "标签");
    private Integer status;
    private String desc;
    CategoryLabelTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CategoryLabelTypeEnum parse(int status) {
        for (CategoryLabelTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer CATEGORY = 1;
        private static final Integer LABEL = 2;
    }
}
