package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 评论类型（1：用户评论，2：系统自动评论）
 *
 * @author xxj
 * @date 2019/3/20
 */
@Getter
public enum AppraiseTypeEnum {
    /** 用户评论*/
    MEMBER(Values.MEMBER),
    /** 系统自动评论*/
    SYSTEM(Values.SYSTEM);

    private final Integer value;

    AppraiseTypeEnum(Integer value) {
        this.value = value;
    }
    public static class Values{
        public static final int MEMBER = 1;
        public static final int SYSTEM = 2;
    }

    public static Integer getValue(AppraiseTypeEnum orderStateEnum) {
        return orderStateEnum.value;
    }

    public static AppraiseTypeEnum getByValue(Integer status){
        for (AppraiseTypeEnum orderStateEnum : values()) {
            if (orderStateEnum.value.equals(status)) {
                return orderStateEnum;
            }
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
