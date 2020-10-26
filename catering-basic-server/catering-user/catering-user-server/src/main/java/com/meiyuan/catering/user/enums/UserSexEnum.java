package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/5 16:27
 */
@Getter
public enum UserSexEnum {
    /**
     * 男
     */
    BOY(Values.BOY, "男"),
    /**
     * 女
     */
    GIRL(Values.GIRL, "女"),
    /**
     * 其他
     */
    NEUTRAL(Values.NEUTRAL, "其他"),
    /**
     * 其他
     */
    NEUTRAL_ZERO(Values.NEUTRAL_ZERO, "其他");

    private Integer status;
    private String desc;
    UserSexEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserSexEnum parse(int status) {
        for (UserSexEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer NEUTRAL_ZERO = 0;
        private static final Integer BOY = 1;
        private static final Integer GIRL = 2;
        private static final Integer NEUTRAL = 3;

    }
}
