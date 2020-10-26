package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * description：活动优惠券枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 10:51
 */
@Getter
public enum ActivityTicketEnum {
    /**
     * 已勾选优惠券
     **/
    YES(Values.YES, Boolean.TRUE,"优惠券"),
    /**
     * 未勾选优惠券
     **/
    NO(Values.NO, Boolean.FALSE,"");
    private Integer status;
    private Boolean flag;
    private String desc;
    ActivityTicketEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.flag = flag;
        this.desc = desc;
    }

    public static ActivityTicketEnum parse(int status) {
        for (ActivityTicketEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }
    public static ActivityTicketEnum parseBool(boolean flag) {
        for (ActivityTicketEnum type : values()) {
            if (type.flag == flag) {
                return type;
            }
        }
        throw new CustomException("非法flag");
    }

    public static class  Values{
        private static final Integer NO = 0;
        private static final Integer YES = 1;
    }
}
