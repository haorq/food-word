package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketSendTicketPartyEnum
 * @Description
 * @Author gz
 * @Date 2020/5/6 11:51
 * @Version 1.1
 */
@Getter
public enum MarketingTicketSendTicketPartyEnum {
    /**
     * 行膳平台
     */
    PLATFORM(Values.PLATFORM, "行膳平台"),
    /**
     * 地推员
     */
    GROUND_PUSHER(Values.GROUND_PUSHER, "地推员"),
    /**
     * 品牌
     */
    BRAND(Values.BRAND, "商家");

    private Integer status;
    private String desc;

    MarketingTicketSendTicketPartyEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketSendTicketPartyEnum parse(Integer status) {

        for (MarketingTicketSendTicketPartyEnum type : values()) {
            if (type.status.compareTo(status) == 0) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static String parseType(Integer status) {
        if (status == null) {
            return "";
        }
        for (MarketingTicketSendTicketPartyEnum value : values()) {
            if (value.status.compareTo(status) == 0) {
                return value.desc;
            }
        }
        return "";
    }

    public static class Values {
        private static final Integer PLATFORM = 1;
        private static final Integer GROUND_PUSHER = 2;
        private static final Integer BRAND = 3;
    }
}
