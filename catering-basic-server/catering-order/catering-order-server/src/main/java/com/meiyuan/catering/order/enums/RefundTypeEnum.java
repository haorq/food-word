package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 退款类型枚举
 *
 * @author zengzhangni
 * @date 2019/4/1
 */
@Getter
public enum RefundTypeEnum {
    /**
     * 退款类型(1：系统退款(默认) 2：店长退款 3：运营/平台退款 4：用户退款)
     */
    SYSTEM(1, "系统退款"),
    SHOP(2, "店长退款"),
    PLATFORM(3, "运营退款"),
    USER(4, "用户退款");

    private final Integer status;

    private final String desc;

    RefundTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String parse(Integer status) {
        for (RefundTypeEnum item : values()) {
            if (status.compareTo(item.getStatus()) == 0) {
                return item.desc;
            }
        }
        return "未知退款类型";
    }
}
