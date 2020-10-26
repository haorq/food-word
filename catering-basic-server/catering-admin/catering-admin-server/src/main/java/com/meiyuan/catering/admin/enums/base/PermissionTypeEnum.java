package com.meiyuan.catering.admin.enums.base;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.Getter;

/**
 * @ClassName PermissionTypeEnum
 * @Description
 * @Author gz
 * @Date 2020/9/29 13:10
 * @Version 1.5.0
 */
@Getter
public enum PermissionTypeEnum {
    /**
     * 平台
     **/
    PLAT_FROM(Values.PLAT_FROM,"平台"),
    /**
     * 商户PC
     **/
    MERCHANT_PC(Values.MERCHANT_PC, "商户PC"),
    /**
     * 商户APP
     */
    MERCHANT_APP(Values.MERCHANT_APP, "商户APP");
    private Integer status;
    private String desc;
    PermissionTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static PermissionTypeEnum parse(int status) {
        for (PermissionTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new CustomException("非法status");
    }

    public static class  Values{
        private static final Integer PLAT_FROM = 2;
        private static final Integer MERCHANT_PC = 0;
        private static final Integer MERCHANT_APP = 1;
    }
}
