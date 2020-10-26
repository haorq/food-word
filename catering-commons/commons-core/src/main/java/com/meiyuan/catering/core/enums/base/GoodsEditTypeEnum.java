package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/09/24 15:09
 * @description 商品修改端枚举
 **/

@Getter
public enum GoodsEditTypeEnum {

    /**
     * 平台端修改
     **/
    WEB(Values.WEB, "平台端编辑"),
    /**
     * 其他端修改
     **/
    OTHER(Values.OTHER, "其他端编辑"),
    /**
     * APP端改价
     */
    PRICE(Values.PRICE, "APP端改价");
    private Integer status;
    private String desc;
    GoodsEditTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsEditTypeEnum parse(int status) {
        for (GoodsEditTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer WEB = 1;
        private static final Integer OTHER = 2;
        private static final Integer PRICE = 3;
    }

}
