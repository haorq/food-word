package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketChildTypeEnum
 * @Description 优惠券类型枚举
 * @Author gz
 * @Date 2020/3/19 14:25
 * @Version 1.1
 */
@Getter
public enum MarketingTicketChildTypeEnum {
    /**
     * 满减券
     */
    DISCOUNT(MarketingTicketChildTypeEnum.Values.DISCOUNT,"满减券"),
    /**
     * 代金券
     */
    VOUCHER(MarketingTicketChildTypeEnum.Values.VOUCHER,"代金券");

    private Integer status;
    private String desc;
    MarketingTicketChildTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketChildTypeEnum parse(int status) {
        for (MarketingTicketChildTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer DISCOUNT = 1;
        private static final Integer VOUCHER = 2;
    }
}
