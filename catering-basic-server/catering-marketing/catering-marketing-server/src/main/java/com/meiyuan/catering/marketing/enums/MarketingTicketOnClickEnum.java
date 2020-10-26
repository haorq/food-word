package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketOnClickEnum
 * @Description
 * @Author gz
 * @Date 2020/4/3 11:07
 * @Version 1.1
 */
@Getter
public enum MarketingTicketOnClickEnum {
    /**
     * 新人注册成功获取
     */
    REGISTER_SUCCESS(MarketingTicketOnClickEnum.Values.REGISTER_SUCCESS,"新人注册成功获取"),
    /**
     * 首单下单成功获取
     */
    ORDER_SUCCESS(MarketingTicketOnClickEnum.Values.ORDER_SUCCESS,"首单下单成功获取"),
    /**
     * 推荐人推荐新用户注册成功获取
     */
    REFERRER_REGISTER_SUCCESS(MarketingTicketOnClickEnum.Values.REFERRER_REGISTER_SUCCESS,"推荐人推荐新用户注册成功获取"),
    /**
     * 推荐人推荐用户下单成功获取
     */
    REFERRER_ORDER_SUCCESS(MarketingTicketOnClickEnum.Values.REFERRER_ORDER_SUCCESS,"推荐人推荐用户下单成功获取"),
    /**
     * 系统无触发条件发券
     */
    NO_ONCLICK(MarketingTicketOnClickEnum.Values.NO_ONCLICK,"系统无触发条件发券");

    private Integer status;
    private String desc;
    MarketingTicketOnClickEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketOnClickEnum parse(int status) {
        for (MarketingTicketOnClickEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer REGISTER_SUCCESS = 2;
        private static final Integer ORDER_SUCCESS = 1;
        private static final Integer REFERRER_REGISTER_SUCCESS = 3;
        private static final Integer REFERRER_ORDER_SUCCESS = 4;
        private static final Integer NO_ONCLICK = 5;
    }
}
