package com.meiyuan.catering.user.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description  用户类型枚举
 * @Date  2020/3/10 0010 11:01
 */
@Getter
public enum UserTypeEnum {
    /**
     * 所有
     */
    ALL(Values.ALL, "所有"),
    /**
     * 企业用户
     */
    COMPANY(Values.COMPANY, "企业用户"),
    /**
     * 个人用户
     */
    PERSONAL(Values.PERSONAL, "个人用户");

    private Integer status;
    private String desc;
    UserTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserTypeEnum parse(int status) {
        for (UserTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.USER_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer ALL = 0;
        private static final Integer COMPANY = 1;
        private static final Integer PERSONAL = 2;
    }
}
