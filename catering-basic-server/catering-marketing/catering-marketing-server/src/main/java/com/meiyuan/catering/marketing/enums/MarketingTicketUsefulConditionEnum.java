package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketUsefulConditionEnum
 * @Description 优惠券使用条件枚举
 * @Author gz
 * @Date 2020/3/19 14:36
 * @Version 1.1
 */
@Getter
public enum  MarketingTicketUsefulConditionEnum {
    /**
     * 订单优惠
     */
    ORDER_TICKET(MarketingTicketUsefulConditionEnum.Values.ORDER_TICKET,"订单优惠"),
    /**
     * 商品优惠
     */
    GOODS_TICKET(MarketingTicketUsefulConditionEnum.Values.GOODS_TICKET,"商品优惠");

    private Integer status;
    private String desc;
    MarketingTicketUsefulConditionEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketUsefulConditionEnum parse(int status) {
        for (MarketingTicketUsefulConditionEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer ORDER_TICKET = 1;
        private static final Integer GOODS_TICKET = 2;
    }
}
