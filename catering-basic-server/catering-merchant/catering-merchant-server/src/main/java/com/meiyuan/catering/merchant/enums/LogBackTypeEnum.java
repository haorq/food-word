package com.meiyuan.catering.merchant.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description  退出登录原因
 * @Date  2020/3/10 0010 13:39
 */
@Getter
public enum LogBackTypeEnum {

    /**
     * 正常登陆 ： 1
     */
    NORMAL(Values.NORMAL, "正常登陆"),
    /**
     * 修改密码 : 2
     **/
    CHANGE_PASSWORD(Values.CHANGE_PASSWORD, "密码修改请重新登陆"),
    /**
     * 修改手机号 ：3
     */
    CHANGE_PHONE(Values.CHANGE_PHONE, "你的账号已被修改"),
    /**
     * 删除账号 ：4
     */
    DEL_ACCOUNT(Values.DEL_ACCOUNT, "您的账号已被删除请联系管理员"),
    /**
     * 禁用 ：5
     */
    DISABLE_ACCOUNT(Values.DISABLE_ACCOUNT, "账号已被禁用，请联系门店负责人");

    private Integer status;
    private String desc;
    LogBackTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static LogBackTypeEnum parse(int status) {
        for (LogBackTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer NORMAL = 1;
        private static final Integer CHANGE_PASSWORD = 2;
        private static final Integer CHANGE_PHONE =3;
        private static final Integer DEL_ACCOUNT =4;
        private static final Integer DISABLE_ACCOUNT =5;
    }
}
