package com.meiyuan.catering.order.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺属性枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum MerchantTypeEnum {
    /**
     * 自营
     */
    SHOP(Values.BUSINESS_SELF, "店铺"),
    /**
     * 入驻
     */
    BUSINESS_POINT(Values.BUSINESS_IN, "自提点"),
    /**
     * 自提点
     */
    SHOP_AND_BUSINESS_POINT(Values.BUSINESS_POINT, "既是店铺也是自提点");
    private Integer status;
    private String desc;
    MerchantTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MerchantTypeEnum parse(int status) {
        for (MerchantTypeEnum type : values()) {
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
