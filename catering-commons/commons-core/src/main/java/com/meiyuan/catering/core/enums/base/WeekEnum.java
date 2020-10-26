package com.meiyuan.catering.core.enums.base;

import lombok.Getter;

/**
 * @author wxf
 * @date 2020/3/31 18:34
 * @description 星期枚举
 **/
@Getter
public enum WeekEnum {
    /**
     * 星期一
     */
    MONDAY("MONDAY", Values.MONDAY, "周一"),
    /**
     * 星期二
     */
    TUESDAY("TUESDAY",Values.TUESDAY,"周二"),
    /**
     * 星期三
     */
    WEDNESDAY("WEDNESDAY",Values.WEDNESDAY,"周三"),
    /**
     * 星期四
     */
    THURSDAY("THURSDAY",Values.THURSDAY,"周四"),
    /**
     * 星期五
     */
    FRIDAY("FRIDAY",Values.FRIDAY,"周五"),
    /**
     * 星期六
     */
    SATURDAY("SATURDAY",Values.SATURDAY,"周六"),
    /**
     * 星期天
     */
    SUNDAY("SUNDAY",Values.SUNDAY,"周日");

    private String code;
    private int status;
    private String desc;

    WeekEnum(String code, Integer status, String desc){
        this.code = code;
        this.status = status;
        this.desc = desc;
    }

    public static WeekEnum parse(int status) {
        for (WeekEnum type : values()) {
            if (type.status == status) {
                return type;
            }
        }
        throw new RuntimeException("非法status");
    }

    public static class  Values{
        private static final Integer MONDAY = 1;
        private static final Integer TUESDAY = 2;
        private static final Integer WEDNESDAY = 3;
        private static final Integer THURSDAY = 4;
        private static final Integer FRIDAY = 5;
        private static final Integer SATURDAY = 6;
        private static final Integer SUNDAY = 7;
    }
}
