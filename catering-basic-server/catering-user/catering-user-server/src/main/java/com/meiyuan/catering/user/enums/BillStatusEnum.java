package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @author yaoozu
 * @description 拼单状态
 * @date 2020/3/2513:58
 * @since v1.0.0
 */
@Getter
public enum BillStatusEnum {
    /**
     * 选购中
     */
    CHOOSING(Values.CHOOSING, "选购中"),
    /**
     * 已点完/结算中
     */
    PAYING(Values.PAYING, "已点完/结算中"),
    /**
     * 已结算
     */
    PAYED(Values.PAYED, "已结算");
    private Integer status;
    private String desc;

    BillStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static BillStatusEnum parse(int status) {
        for (BillStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

    public static class Values {
        private static final Integer CHOOSING = 1;
        private static final Integer PAYING = 2;
        private static final Integer PAYED = 3;
    }
}
