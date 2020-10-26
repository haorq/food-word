package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：广告是否显示枚举
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/2 17:04
 */
@Getter
public enum AdvertisingShowsEnum {
    /**
     *显示
     **/
    SHOW(Values.SHOW, Boolean.TRUE, "显示"),
    /**
     *隐藏
     **/
    NOT_SHOW(Values.NOT_SHOW, Boolean.FALSE,"隐藏");

    private Integer status;
    private String desc;
    private Boolean flag;
    AdvertisingShowsEnum(Integer status, Boolean flag, String desc){
        this.status = status;
        this.desc = desc;
        this.flag = flag;
    }
    public static AdvertisingShowsEnum parse(int status) {
        for (AdvertisingShowsEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer SHOW = 1;
        private static final Integer NOT_SHOW = 0;
    }
}
