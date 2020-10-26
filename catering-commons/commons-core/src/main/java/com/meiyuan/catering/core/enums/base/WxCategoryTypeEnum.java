package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：小程序类目类型枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 10:09
 */
@Getter
public enum WxCategoryTypeEnum {
    /**
     *导航栏
     **/
    NAV_BAR(Values.NAV_BAR, "导航栏"),
    /**
     *推荐区
     **/
    RECOMMEND(Values.RECOMMEND, "推荐区"),
    /**
     *爆品推荐
     **/
    HOT_MONEY(Values.HOT_MONEY, "爆品推荐");

    private Integer status;
    private String desc;
    WxCategoryTypeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static WxCategoryTypeEnum parse(int status) {
        for (WxCategoryTypeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer NAV_BAR = 1;
        private static final Integer RECOMMEND = 2;
        private static final Integer HOT_MONEY = 3;
    }
}
