package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * description：用户来源枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/7 18:08
 */
@Getter
public enum PullNewUserEnum {
    /**
     * 自然流量
     */
    NON_EXISTENT(Values.NON_EXISTENT, "自然流量"),
    /**
     * 地推
     */
    PUSH(Values.PUSH, "地推"),
    /**
     * 被邀请
     */
    INVITED(Values.INVITED, "被邀请");

    private Integer status;
    private String desc;
    PullNewUserEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static PullNewUserEnum parse(int status) {
        for (PullNewUserEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer NON_EXISTENT = 0;
        private static final Integer PUSH = 1;
        private static final Integer INVITED = 2;
    }
}
