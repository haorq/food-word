package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * description：活动评价规则枚举
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/11 9:27
 */
@Getter
public enum ActivityEvaluateEnum {

    /**
     *图片加文字
     **/
    PICTURE_AND_WRITING(Values.PICTURE_AND_WRITING, "图片加文字"),
    /**
     *仅文字
     **/
    WRITING(Values.WRITING, "仅文字"),
    /**
     *仅图片
     **/
    PICTURE(Values.PICTURE, "仅图片");

    private Integer status;
    private String desc;
    ActivityEvaluateEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static ActivityEvaluateEnum parse(int status) {
        for (ActivityEvaluateEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer PICTURE_AND_WRITING = 3;
        private static final Integer WRITING = 2;
        private static final Integer PICTURE = 1;
    }
}
