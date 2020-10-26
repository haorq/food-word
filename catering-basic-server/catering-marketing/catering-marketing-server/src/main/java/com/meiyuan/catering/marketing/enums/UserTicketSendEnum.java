package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/11 10:49
 */
@Getter
public enum UserTicketSendEnum {
    /**
     * 行善平台
     **/
    PLATFORM_TICKET(UserTicketSendEnum.Values.PLATFORM_TICKET, "行善平台"),
    /**
     * 品牌
     **/
    MERCHANT_TICKET(UserTicketSendEnum.Values.MERCHANT_TICKET, "品牌");

    private Integer status;
    private String desc;
    UserTicketSendEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserTicketSendEnum parse(int status) {
        for (UserTicketSendEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer PLATFORM_TICKET = 1;
        private static final Integer MERCHANT_TICKET = 3;
    }
}
