package com.meiyuan.catering.core.enums.base.merchant;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 外卖单据类型
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum TicketTypeEnum {
    /**
     * 外卖单
     **/
    OUT_TICKET(Values.OUT_TICKET, "外卖单"),
    /**
     * 厨打单
     **/
    KITCHEN_TICKET(Values.KITCHEN_TICKET, "厨打单"),
    /**
     * 外卖单兼厨打单
     */
    ALL(Values.ALL, "外卖单兼厨打单");

    private Integer status;
    private String desc;
    TicketTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static TicketTypeEnum parse(int status) {
        for (TicketTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer OUT_TICKET = 1;
        private static final Integer KITCHEN_TICKET = 2;
        private static final Integer ALL = 3;
    }
}
