package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName MarketingTicketIndateTypeEnum
 * @Description 优惠券有效期类型枚举
 * @Author gz
 * @Date 2020/6/2 16:28
 * @Version 1.1
 */
@Getter
public enum MarketingTicketIndateTypeEnum {
    /**
     * 日期
     */
    DATE(MarketingTicketIndateTypeEnum.Values.DATE,"日期"),
    /**
     * 天数
     */
    DAYS(MarketingTicketIndateTypeEnum.Values.DAYS,"天数");

    private Integer status;
    private String desc;
    MarketingTicketIndateTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MarketingTicketIndateTypeEnum parse(int status) {
        for (MarketingTicketIndateTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer DAYS = 2;
        private static final Integer DATE = 1;
    }

}
