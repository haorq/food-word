package com.meiyuan.catering.core.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @Author MeiTao
 * @Description 服务类型枚举
 * @Date  2020/3/10 0010 11:50
 */
@Getter
public enum ServiceTypeEnum {
    /**
     * 都不可以
     */
    ALL_NOT(Values.ALL_NOT, "都不可以"),
    /**
     * 外卖小程序
     */
    WX(Values.WX, "外卖小程序"),
    /**
     * 堂食美食城
     */
    TS(Values.TS, "堂食美食城"),

    WX_TS(Values.WX_TS, "外卖小程序兼堂食美食城");
    private Integer status;
    private String desc;
    ServiceTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ServiceTypeEnum parse(int status) {
        for (ServiceTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class  Values{
        private static final Integer ALL_NOT = 0;
        private static final Integer WX = 1;
        private static final Integer TS = 2;
        private static final Integer WX_TS = 3;
    }
}
