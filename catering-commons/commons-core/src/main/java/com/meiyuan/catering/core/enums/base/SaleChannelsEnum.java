package com.meiyuan.catering.core.enums.base;

import lombok.Getter;


/**
 * @author lhm
 * @date 2020/7/11
 * @description
 **/
@Getter
public enum  SaleChannelsEnum {

    /**
     * 外卖
     **/
    TAKEOUT(SaleChannelsEnum.Values.TAKEOUT, "外卖"),
    /**
     * 堂食
     **/
    TS(SaleChannelsEnum.Values.TS, "堂食"),

    /**
     * 全部
     */
    ALL(SaleChannelsEnum.Values.ALL, "全部");
    private Integer status;
    private String desc;
    SaleChannelsEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static SaleChannelsEnum parse(int status) {
        for (SaleChannelsEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer TAKEOUT = 1;
        private static final Integer TS = 2;
        private static final Integer ALL = 3;
    }

}
