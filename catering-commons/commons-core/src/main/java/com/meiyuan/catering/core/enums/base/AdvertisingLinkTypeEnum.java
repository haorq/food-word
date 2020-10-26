package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：广告跳转类型枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/9 17:01
 */
@Getter
public enum AdvertisingLinkTypeEnum {

    /**
     * 内部地址
     **/
    INSIDE(Values.INSIDE, null, "内部地址"),
    /**
     * 自定义页面
     **/
    CUSTOMIZE(Values.CUSTOMIZE, Url.CUSTOMIZE_URL, "自定义页面"),
    /**
     * 无
     **/
    NOTHING(Values.NOTHING, null, "无"),;

    private Integer status;
    private String desc;
    private String url;

    AdvertisingLinkTypeEnum(Integer status, String url, String desc) {
        this.status = status;
        this.desc = desc;
        this.url = url;
    }

    public static AdvertisingLinkTypeEnum parse(Integer status) {
        for (AdvertisingLinkTypeEnum type : values()) {
            if (type.status.equals(status)) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class Values {
        private static final Integer NOTHING = 0;
        private static final Integer INSIDE = 1;
        private static final Integer CUSTOMIZE = 2;
    }

    public static class Url{
        // 二级页面跳转地址
        private static final String CUSTOMIZE_URL = "/hhpages/home/bannerad/index?id=";
    }
}
