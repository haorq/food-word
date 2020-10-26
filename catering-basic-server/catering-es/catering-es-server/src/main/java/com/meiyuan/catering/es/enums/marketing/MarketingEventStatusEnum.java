package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/10 14:08
 * @description 场次状态枚举
 **/

@Getter
public enum MarketingEventStatusEnum {

    /**
     * 即将开始
     **/
    NOT_START(Values.NOT_START, "未开抢"),
    /**
     * 即将开抢
     */
    SOON_START(Values.SOON_START, "即将开抢"),
    /**
     * 进行中
     **/
    START(Values.START, "已开抢"),
    /**
     * 正在疯抢
     */
    FRENZIED_START(Values.FRENZIED_START, "正在疯抢"),
    /**
     * 已结束
     */
    END(Values.END, "已结束"),
    /**
     * 明日预告
     **/
    TOMORROW(Values.TOMORROW, "明日预告");

    private Integer status;
    private String desc;

    MarketingEventStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingEventStatusEnum parse(int status) {
        for (MarketingEventStatusEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer NOT_START = 1;
        private static final Integer SOON_START = 2;
        private static final Integer START = 3;
        private static final Integer FRENZIED_START = 4;
        private static final Integer END = 5;
        private static final Integer TOMORROW = 6;
    }

}
