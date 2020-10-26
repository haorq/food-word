package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺配送时间范围类型:1：店铺配送时间范围，2：店铺自提时间范围
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopDeliveryTypeEnum {
    /**
     * 店铺自提时间范围
     */
    PICKUP(Values.PICKUP, "店铺自提时间范围"),
    /**
     * 店铺配送时间范围
     */
    DELIVERY(Values.DELIVERY, "店铺配送时间范围");

    private Integer status;
    private String desc;
    ShopDeliveryTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopDeliveryTypeEnum parse(int status) {
        for (ShopDeliveryTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer DELIVERY = 1;
        private static final Integer PICKUP = 2;
    }
}
