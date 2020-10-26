package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺配送对象枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopDeliveryObjectEnum {
    /**
     * 企业
     */
    COMPANY(Values.COMPANY, "企业"),
    /**
     * 个人
     */
    PERSONAL(Values.PERSONAL, "个人"),
    /**
     * 全部
     */
    ALL(Values.ALL, "全部");
    private Integer status;
    private String desc;
    ShopDeliveryObjectEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopDeliveryObjectEnum parse(int status) {
        for (ShopDeliveryObjectEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer COMPANY = 1;
        private static final Integer PERSONAL = 2;
        private static final Integer ALL = 3;
    }
}
