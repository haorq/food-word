package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingOfTypeEnum
 * @Description 促销活动关联类型枚举
 * @Author gz
 * @Date 2020/3/16 11:04
 * @Version 1.1
 */
@Getter
public enum MarketingOfTypeEnum {

    /**
     * 秒杀
     **/
    SECKILL(Values.SECKILL, "秒杀"),
    /**
     * 拼团
     **/
    GROUP(Values.GROUP, "拼团"),
    /**
     * 团购
     **/
    GROUPON(Values.GROUPON, "团购"),
    /**
     * 优惠券
     **/
    TICKET(Values.TICKET, "优惠券"),

    SPECIAL(Values.SPECIAL, "特价");

    private Integer status;
    private String desc;

    MarketingOfTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingOfTypeEnum parse(int status) {
        for (MarketingOfTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer SECKILL = 1;
        private static final Integer GROUP = 2;
        private static final Integer GROUPON = 3;
        private static final Integer TICKET = 4;
        private static final Integer SPECIAL = 5;
    }

}
