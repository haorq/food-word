package com.meiyuan.catering.es.enums.goods;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/10 10:59
 * @description 商品上下架枚举
 **/
@Getter
public enum GoodsStatusEnum {
    /**
     * 下架
     **/
    LOWER_SHELF(Values.LOWER_SHELF, "下架"),
    /**
     * 上架
     **/
    UPPER_SHELF(Values.UPPER_SHELF, "上架");
    private Integer status;
    private String desc;
    GoodsStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsStatusEnum parse(int status) {
        for (GoodsStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer LOWER_SHELF = 1;
        private static final Integer UPPER_SHELF = 2;
    }
}
