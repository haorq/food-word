package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/6/1 11:47
 * @description 简单描述
 **/
@Getter
public enum SortTypeEnum {
    /**
     * 分类
     **/
    CATEGORY(Values.CATEGORY, "分类"),
    /**
     * 商品
     **/
    GOODS(Values.GOODS, "商品");
    private Integer status;
    private String desc;
    SortTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static SortTypeEnum parse(int status) {
        for (SortTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer CATEGORY = 1;
        private static final Integer GOODS = 2;
    }
}
