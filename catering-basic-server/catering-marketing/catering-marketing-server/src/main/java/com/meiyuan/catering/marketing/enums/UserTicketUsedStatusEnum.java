package com.meiyuan.catering.marketing.enums;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName UserTicketUsedStatusEnum
 * @Description 用户优惠券使用情况枚举
 * @Author gz
 * @Date 2020/4/13 14:46
 * @Version 1.1
 */
@Getter
public enum UserTicketUsedStatusEnum {
    /**
     * 全部
     **/
    NO_USED(UserTicketUsedStatusEnum.Values.NO_USED, "待使用"),
    /**
     * 个人
     **/
    USED(UserTicketUsedStatusEnum.Values.USED, "已使用"),
    /**
     * 企业
     **/
    EXPIRED(UserTicketUsedStatusEnum.Values.EXPIRED, "已过期");
    private Integer status;
    private String desc;
    UserTicketUsedStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserTicketUsedStatusEnum parse(int status) {
        for (UserTicketUsedStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer NO_USED = 1;
        private static final Integer USED = 2;
        private static final Integer EXPIRED = 3;
    }
}
