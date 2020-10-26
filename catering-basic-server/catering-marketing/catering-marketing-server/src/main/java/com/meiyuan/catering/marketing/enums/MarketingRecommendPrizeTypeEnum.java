package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 推荐有奖活动奖品类型枚举
 **/
@Getter
public enum MarketingRecommendPrizeTypeEnum {
    /**
     * 积分
     */
    INTEGRAL(Values.INTEGRAL, "积分"),
    /**
     * 优惠券
     */
    COUPON(Values.COUPON, "优惠券");

    private Integer status;
    private String desc;

    MarketingRecommendPrizeTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingRecommendPrizeTypeEnum parse(int status) {
        for (MarketingRecommendPrizeTypeEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer INTEGRAL = 1;
        private static final Integer COUPON = 2;
    }
}
