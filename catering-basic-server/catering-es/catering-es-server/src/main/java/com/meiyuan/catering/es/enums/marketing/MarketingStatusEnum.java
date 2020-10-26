package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 团购活动状态枚举
 **/
@Getter
public enum MarketingStatusEnum {
    /**
     * 未开始
     **/
    NOT_START(Values.NOT_START, "未开始"),
    /**
     * 进行中
     **/
    ONGOING(Values.ONGOING, "进行中"),
    /**
     * 已结束
     **/
    ENDED(Values.ENDED, "已结束");

    private Integer status;
    private String desc;

    MarketingStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingStatusEnum parse(int status) {
        for (MarketingStatusEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer NOT_START = 2;
        private static final Integer ONGOING = 1;
        private static final Integer ENDED = 3;
    }
}
