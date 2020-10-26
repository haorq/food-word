package com.meiyuan.catering.es.enums.marketing;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/09/02 11:09
 * @description 营销活动类型枚举
 **/

@Getter
public enum MarketingTypeEnum {

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
    /**
     * 特价商品活动
     */
    SPECIAL(Values.SPECIAL, "特价商品");
    private Integer status;
    private String desc;
    MarketingTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTypeEnum parse(int status) {
        for (MarketingTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer SECKILL = 1;
        private static final Integer GROUP = 2;
        private static final Integer GROUPON = 3;
        private static final Integer TICKET = 4;
        private static final Integer SPECIAL = 5;
    }

}
