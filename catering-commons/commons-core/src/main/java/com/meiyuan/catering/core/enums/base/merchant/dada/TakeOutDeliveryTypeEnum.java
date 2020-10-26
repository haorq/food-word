package com.meiyuan.catering.core.enums.base.merchant.dada;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 外卖配送类型： 1-自配送、2-达达配送
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum TakeOutDeliveryTypeEnum {
    /**
     * 1-自配送 : 商家自己配送，不使用第三方配送
     **/
    SELF(Values.SELF, "1-自配送"),
    /**
     * 2-达达配送
     */
    DADA(Values.DADA, "2-达达配送");

    private Integer status;
    private String desc;
    TakeOutDeliveryTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static TakeOutDeliveryTypeEnum parse(int status) {
        for (TakeOutDeliveryTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer SELF = 1;
        private static final Integer DADA = 2;
    }
}
