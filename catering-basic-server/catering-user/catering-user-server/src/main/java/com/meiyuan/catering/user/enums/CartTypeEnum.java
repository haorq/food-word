package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @author yaoozu
 * @description 购物车类型
 * @date 2020/3/2514:08
 * @since v1.0.0
 */
@Getter
public enum CartTypeEnum {
    /**
     * 1:普通购物车
     */
    ORDINARY(1, "普通"),
    /**
     * 1:拼单购物车
     */
    SHARE_BILL(2, "拼单");
    private Integer status;
    private String desc;

    CartTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CartTypeEnum parse(int status) {
        for (CartTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

}
