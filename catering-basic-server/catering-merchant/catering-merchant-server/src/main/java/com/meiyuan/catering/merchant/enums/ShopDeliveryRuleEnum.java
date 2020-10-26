package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 店铺配送规则枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ShopDeliveryRuleEnum {
    /**
     * 距离
     */
    DISTANCE(Values.DISTANCE, "距离"),
    /**
     * 直线
     */
    FIXATION(Values.FIXATION, "直线");

    private Integer status;
    private String desc;
    ShopDeliveryRuleEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ShopDeliveryRuleEnum parse(int status) {
        for (ShopDeliveryRuleEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer DISTANCE = 1;
        private static final Integer FIXATION = 2;
    }
}
