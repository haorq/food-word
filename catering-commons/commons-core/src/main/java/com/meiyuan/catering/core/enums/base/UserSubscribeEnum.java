package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：用户是否已关注标识
 *
 * @author yy
 * @version 1.4.0
 * @date 2020/9/4 14:13
 */
@Getter
public enum UserSubscribeEnum {
    /**
     * 已关注
     **/
    
    FOCUS_ON(Values.FOCUS_ON, "已关注"),
    /**
     * 未关注
     **/
    NOT_FOCUS_ON(Values.NOT_FOCUS_ON, "未关注");
    private Integer status;
    private String desc;
    UserSubscribeEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserSubscribeEnum parse(int status) {
        for (UserSubscribeEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer NOT_FOCUS_ON = 0;
        private static final Integer FOCUS_ON = 1;
    }
}
