package com.meiyuan.catering.es.enums.goods;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/09/07 15:09
 * @description 商品特价标识枚举
 **/

@Getter
public enum GoodsSpecialStateEnum {

    /**
     * 特价商品
     **/
    TRUE(Values.TRUE, "特价商品"),
    /**
     * 非特价商品
     **/
    FALSE(Values.FALSE, "非特价商品");
    private Boolean status;
    private String desc;
    GoodsSpecialStateEnum(Boolean status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsSpecialStateEnum parse(Boolean status) {
        for (GoodsSpecialStateEnum type : values()) {
            if (type.status.equals(status)) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Boolean TRUE = true;
        private static final Boolean FALSE = false;
    }

}
