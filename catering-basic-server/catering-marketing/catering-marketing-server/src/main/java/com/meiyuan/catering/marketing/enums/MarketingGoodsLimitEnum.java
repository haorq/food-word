package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author luohuan
 * @date 2020/3/18
 * 活动商品使用限制
 **/
@Getter
public enum MarketingGoodsLimitEnum {
    /**
     * 不限制
     */
    ALL(Values.ALL, "不限制"),
    /**
     * 指定商品
     */
    GOODS(Values.GOODS, "指定商品"),
    /**
     * 指定商品类型
     */
    GOODS_TYPE(Values.GOODS_TYPE, "指定商品类型");

    private Integer status;
    private String desc;

    MarketingGoodsLimitEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingGoodsLimitEnum parse(int status) {
        for (MarketingGoodsLimitEnum statusEnum : values()) {
            if (statusEnum.status == status) {
                return statusEnum;
            }
        }
        throw new CustomException("非法status");
    }

    public static class Values {
        private static final Integer ALL = 1;
        private static final Integer GOODS = 2;
        private static final Integer GOODS_TYPE = 3;
    }
}
