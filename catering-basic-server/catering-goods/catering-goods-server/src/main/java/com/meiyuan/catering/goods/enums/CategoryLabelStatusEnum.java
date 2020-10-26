package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/19 11:03
 * @description 分类标签状态枚举
 **/
@Getter
public enum CategoryLabelStatusEnum {
    /**
     * 禁用
     **/
    NO(Values.NO, "禁用"),
    /**
     * 启用
     **/
    YES(Values.YES, "启用");
    private Integer status;
    private String desc;
    CategoryLabelStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static CategoryLabelStatusEnum parse(int status) {
        for (CategoryLabelStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer NO = 1;
        private static final Integer YES = 2;
    }
}
