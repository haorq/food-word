package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺类型以及店铺本身是否是自提点：1--店铺，2--自提点，3--既是店铺也是自提点
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopIsPickupEnum {

    /**
     * 店铺
     **/
    SHOP(Values.SHOP,"店铺"),
    /**
     * 自提点
     **/
    PICKUP(Values.PICKUP,"自提点"),
    /**
     * 既是店铺也是自提点
     **/
    SHOP_AND_PICKUP(Values.SHOP_AND_PICKUP,"既是店铺也是自提点");

    private Integer status;
    private String desc;
    ShopIsPickupEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopIsPickupEnum parse(int status) {
        for (ShopIsPickupEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer SHOP = 1;
        private static final Integer PICKUP = 2;
        private static final Integer SHOP_AND_PICKUP = 3;
    }
}
