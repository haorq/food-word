package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @author lhm
 * @date 2020/7/11
 * @description
 **/
@Getter
public enum  GoodsAddTypeEnum {

    /**
     * 平台推送
     **/
    PLANTE(GoodsAddTypeEnum.Values.PLANTE, "平台推送"),
    /**
     * 商户自创
     **/
    MERCHANT(GoodsAddTypeEnum.Values.MERCHANT, "商户自创"),
    /**
     * 门店自创
     **/
    SHOP(GoodsAddTypeEnum.Values.SHOP, "门店自创");
    private Integer status;
    private String desc;
    GoodsAddTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static GoodsAddTypeEnum parse(int status) {
        for (GoodsAddTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer PLANTE = 1;
        private static final Integer MERCHANT = 2;
        private static final Integer SHOP = 3;
    }
}
