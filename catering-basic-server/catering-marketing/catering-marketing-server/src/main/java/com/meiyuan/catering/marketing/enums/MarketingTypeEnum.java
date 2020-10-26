package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author GongJunZheng
 * @date 2020/08/05 15:08
 * @description 营销活动类型
 **/

@Getter
public enum  MarketingTypeEnum {

    /**
     * 全部活动
     */
    ALL(Values.ALL, "全部"),
    /**
     * 限时秒杀活动
     */
    SECKILL(Values.SECKILL, "限时秒杀"),
    /**
     * 商品团购活动
     */
    GROUPON(Values.GROUPON, "商品团购"),
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

    public static class Values{
        private static final Integer ALL = 0;
        private static final Integer SECKILL = 1;
        private static final Integer GROUPON = 3;
        private static final Integer SPECIAL = 5;
    }

}
