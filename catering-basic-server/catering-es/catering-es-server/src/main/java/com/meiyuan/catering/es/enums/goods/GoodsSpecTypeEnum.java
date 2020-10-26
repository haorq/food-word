package com.meiyuan.catering.es.enums.goods;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/19 11:43
 * @description 商品规格类型枚举
 **/
@Getter
public enum GoodsSpecTypeEnum {
    /**
     * 统一规格
     **/
    UNIFIED_SPEC(Values.UNIFIED_SPEC, "统一规格"),
    /**
     * 多规格
     **/
    MANY_SPEC(Values.MANY_SPEC, "多规格");
    private Integer status;
    private String desc;
    GoodsSpecTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsSpecTypeEnum parse(int status) {
        for (GoodsSpecTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer UNIFIED_SPEC = 1;
        private static final Integer MANY_SPEC = 2;
    }
}
