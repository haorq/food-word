package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺属性枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopTypeEnum {
    /**
     * 店铺
     */
    BUSINESS_SELF(Values.BUSINESS_SELF, "店铺"),
    /**
     * 店铺兼自提点
     */
    BUSINESS_IN(Values.BUSINESS_IN, "店铺兼自提点"),
    /**
     * 自提点
     */
    BUSINESS_POINT(Values.BUSINESS_POINT, "自提点");
    private Integer status;
    private String desc;
    ShopTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopTypeEnum parse(int status) {
        for (ShopTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer BUSINESS_SELF = 1;
        private static final Integer BUSINESS_IN = 2;
        private static final Integer BUSINESS_POINT = 3;
    }
}
