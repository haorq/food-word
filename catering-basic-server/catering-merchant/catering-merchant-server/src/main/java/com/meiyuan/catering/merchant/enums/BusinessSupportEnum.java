package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 业务支持：1：仅配送，2：仅自提，3：全部  枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum BusinessSupportEnum {
    /**
     * 仅配送
     */
    DELIVERY(Values.DELIVERY, "仅配送"),
    /**
     * 仅自提
     */
    PICKUP(Values.PICKUP, "仅自提"),
    /**
     * 全部
     */
    ALL(Values.ALL, "全部"),
    /**
     * 查询门店支持类型
     */
    GAIN_SUPPORT_TYPE(Values.GAIN_SUPPORT_TYPE, "查询门店支持类型");
    private Integer status;
    private String desc;
    BusinessSupportEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static BusinessSupportEnum parse(int status) {
        for (BusinessSupportEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer DELIVERY = 1;
        private static final Integer PICKUP = 2;
        private static final Integer ALL = 3;
        private static final Integer GAIN_SUPPORT_TYPE = 4;
    }
}
