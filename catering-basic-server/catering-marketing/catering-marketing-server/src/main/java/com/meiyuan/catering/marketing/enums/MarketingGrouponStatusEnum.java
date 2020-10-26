package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 团购活动状态枚举
 **/
@Getter
public enum MarketingGrouponStatusEnum {
    /**
     * 未开始
     */
    NOT_START(Values.NOT_START, "未开始"),
    /**
     * 进行中
     */
    ONGOING(Values.ONGOING, "进行中"),
    /**
     * 已结束
     */
    ENDED(Values.ENDED, "已结束"),

    /**
     * 已冻结
     */
    FREEZE(Values.FREEZE, "已冻结");

    private Integer status;
    private String desc;

    MarketingGrouponStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingGrouponStatusEnum parse(int status) {
        for (MarketingGrouponStatusEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer NOT_START = 1;
        private static final Integer ONGOING = 2;
        private static final Integer ENDED = 3;
        private static final Integer FREEZE = 4;

    }
}
