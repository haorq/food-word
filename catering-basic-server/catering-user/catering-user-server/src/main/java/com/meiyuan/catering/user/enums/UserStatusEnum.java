package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * description：用户状态枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/19 14:44
 */
@Getter
public enum UserStatusEnum {
    /**
     * 注销 
     */
    CANCEL(Values.CANCEL, "注销"),
    /**
     * 正常
     */
    NORMAL(Values.NORMAL, "正常");

    private Integer status;
    private String desc;
    UserStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserStatusEnum parse(int status) {
        for (UserStatusEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer CANCEL = 2;
        private static final Integer NORMAL = 1;
    }
}
