package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketGoodsLimitEnum
 * @Description 优惠券商品限制枚举
 * @Author gz
 * @Date 2020/3/19 14:28
 * @Version 1.1
 */
@Getter
public enum MarketingTicketGoodsLimitEnum {
    /**
     * 不限制
     */
    NO_LIMIT(MarketingTicketGoodsLimitEnum.Values.NO_LIMIT,"不限制"),
    /**
     * 限制分类
     */
    LIMIT_CLASSIFY(MarketingTicketGoodsLimitEnum.Values.LIMIT_CLASSIFY,"限制分类"),
    /**
     * 限制商品
     */
    LIMIT_GOODS(MarketingTicketGoodsLimitEnum.Values.LIMIT_GOODS,"限制商品");

    private Integer status;
    private String desc;
    MarketingTicketGoodsLimitEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketGoodsLimitEnum parse(int status) {
        for (MarketingTicketGoodsLimitEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer NO_LIMIT = 1;
        private static final Integer LIMIT_GOODS = 2;
        private static final Integer LIMIT_CLASSIFY = 3;
    }
}
