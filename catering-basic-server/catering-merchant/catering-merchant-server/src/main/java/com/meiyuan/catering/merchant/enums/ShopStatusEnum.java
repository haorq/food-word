package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author lh
 * @Description 门店状态枚举
 * @Date 2020/7/10 0010 13:39
 */
@Getter
public enum ShopStatusEnum {
    /**
     * 启用
     **/
    OPEN(Values.OPEN, "启用"),
    /**
     * 禁用
     **/
    FORBIDDEN(Values.FORBIDDEN, "禁用"),
    /**
     * 删除
     **/
    DELETE(Values.DELETE, "删除");
    private Integer status;
    private String desc;
    ShopStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopStatusEnum parse(int status) {
        for (ShopStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class Values {
        private static final Integer OPEN = 1;
        private static final Integer FORBIDDEN = 2;
        private static final Integer DELETE = 3;
    }
}
