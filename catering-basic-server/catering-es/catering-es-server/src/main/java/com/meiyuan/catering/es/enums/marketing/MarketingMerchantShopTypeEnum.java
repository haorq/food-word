package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/26 09:08
 * @description 商户、店铺类型枚举
 **/

@Getter
public enum MarketingMerchantShopTypeEnum {

    /**
     * 商户
     **/
    MERCHANT(Values.MERCHANT, "商户"),
    /**
     * 店铺
     **/
    SHOP(Values.SHOP, "店铺");

    private Integer status;

    private String desc;

    MarketingMerchantShopTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingMerchantShopTypeEnum parse(int status) {
        for (MarketingMerchantShopTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer MERCHANT = 1;
        private static final Integer SHOP = 2;
    }

}
