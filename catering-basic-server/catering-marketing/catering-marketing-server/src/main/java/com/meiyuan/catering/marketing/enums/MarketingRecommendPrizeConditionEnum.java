package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 推荐有奖活动奖品类型枚举
 **/
@Getter
public enum MarketingRecommendPrizeConditionEnum {
    /**
     * 新人注册成功
     */
    REGISTER_SUCCESS(Values.REGISTER_SUCCESS, "新人注册成功"),
    /**
     * 下单成功
     */
    ORDER_SUCCESS(Values.ORDER_SUCCESS, "下单成功");

    private Integer status;
    private String desc;

    MarketingRecommendPrizeConditionEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingRecommendPrizeConditionEnum parse(int status) {
        for (MarketingRecommendPrizeConditionEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer REGISTER_SUCCESS = 1;
        private static final Integer ORDER_SUCCESS = 2;
    }
}
