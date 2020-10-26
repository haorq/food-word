package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 查询类型:1:查询已绑定自提点，2：查询未绑定自提点
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum BoundTypeEnum {
    /**
     * 查询已绑定自提点
     */
    BOUND(Values.BOUND, "查询已绑定自提点"),
    /**
     * 查询未绑定自提点
     */
    NOT_BOUND(Values.NOT_BOUND, "查询未绑定自提点");

    private Integer status;
    private String desc;
    BoundTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static BoundTypeEnum parse(int status) {
        for (BoundTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer BOUND = 1;
        private static final Integer NOT_BOUND = 2;
    }
}
