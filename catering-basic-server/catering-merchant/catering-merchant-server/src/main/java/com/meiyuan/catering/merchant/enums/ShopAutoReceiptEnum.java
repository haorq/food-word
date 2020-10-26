package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺是否自动接单枚举
 * @Date  2020/3/10 0010 13:58
 */
@Getter
public enum ShopAutoReceiptEnum {
    /**
     * 自动接单
     */
    AUTO_RECEIPT(Values.AUTO_RECEIPT, "自动接单"),
    /**
     * 不自动接单
     */
    NOT_AUTO_RECEIPT(Values.NOT_AUTO_RECEIPT, "不自动接单");
    private Integer status;
    private String desc;
    ShopAutoReceiptEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopAutoReceiptEnum parse(int status) {
        for (ShopAutoReceiptEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer AUTO_RECEIPT = 1;
        private static final Integer NOT_AUTO_RECEIPT = 2;
    }
}
