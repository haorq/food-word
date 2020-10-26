package com.meiyuan.catering.merchant.goods.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author wxf
 * @date 2019/3/10
 * @description 商品新增类型枚举
 */
@Getter
public enum GoodsAddTypeEnum {
    /**
     * 平台（加工厂）
     **/
    PLATFORM(GoodsAddTypeEnum.Values.PLATFORM, "平台推送"),
    /**
     * 商家
     **/
    MERCHANT(GoodsAddTypeEnum.Values.MERCHANT, "商家自创"),


    /**
     * 门店
     */
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
        throw new CustomException("非法status");
    }
     private static class  Values{
        private static final Integer PLATFORM = 1;
        private static final Integer MERCHANT = 2;
        private static final Integer SHOP = 3;
    }
}
