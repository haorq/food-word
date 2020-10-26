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
public enum MarketingTicketActivityTypeEnum {
    /**
     * 店内领券
     */
    SHOP_IN_GET(Values.SHOP_IN_GET,"店内领券"),
    /**
     * 店外发券
     */
    SHOP_OUT_GET(Values.SHOP_OUT_GET,"店外发券"),
    /**
     * 平台补贴
     */
    PLATFORM_SUBSIDY(Values.PLATFORM_SUBSIDY,"平台补贴");

    private Integer status;
    private String desc;
    MarketingTicketActivityTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketActivityTypeEnum parse(int status) {
        for (MarketingTicketActivityTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer SHOP_IN_GET = 1;
        private static final Integer SHOP_OUT_GET = 2;
        private static final Integer PLATFORM_SUBSIDY = 3;
    }
}
