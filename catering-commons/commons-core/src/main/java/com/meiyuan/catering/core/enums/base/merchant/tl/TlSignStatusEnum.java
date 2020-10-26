package com.meiyuan.catering.core.enums.base.merchant.tl;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 门店与通联签约状态： 1：签约，2：未签约
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum TlSignStatusEnum {
    /**
     * 1-签约
     **/
    SIGN(Values.SIGN, "1-签约"),
    /**
     * 2-电子签约
     */
    NOT_SIGN(Values.NOT_SIGN, "2-未签约");

    private Integer status;
    private String desc;
    TlSignStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static TlSignStatusEnum parse(int status) {
        for (TlSignStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer SIGN = 1;
        private static final Integer NOT_SIGN = 2;
    }
}
