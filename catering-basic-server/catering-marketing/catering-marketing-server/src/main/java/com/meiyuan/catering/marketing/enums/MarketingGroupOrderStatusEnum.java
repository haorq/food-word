package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 团单状态枚举
 **/
@Getter
public enum MarketingGroupOrderStatusEnum {
    /**
     * 拼团中
     */
    PROCESSING(Values.PROCESSING, "拼团中"),
    /**
     * 拼团成功
     */
    SUCCESS(Values.SUCCESS, "拼团成功"),
    /**
     * 拼团失败
     */
    FAILURE(Values.FAILURE, "拼团失败");

    private Integer status;
    private String desc;

    MarketingGroupOrderStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingGroupOrderStatusEnum parse(int status) {
        for (MarketingGroupOrderStatusEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer PROCESSING = 1;
        private static final Integer SUCCESS = 2;
        private static final Integer FAILURE = 3;
    }
}
