package com.meiyuan.catering.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/19 18:18
 * @description 商品限购枚举
 **/
@Getter
public enum GoodsBuyLimitEnum {
    /**
     * 无限制
     **/
    UNLIMITED(Values.UNLIMITED, "无限制"),
    /**
     * 自定义
     **/
    CUSTOM(Values.CUSTOM, "自定义");
    private Integer status;
    private String desc;
    GoodsBuyLimitEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsBuyLimitEnum parse(int status) {
        for (GoodsBuyLimitEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer UNLIMITED= 1;
        private static final Integer CUSTOM= 2;
    }
}
