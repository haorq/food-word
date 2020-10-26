package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 退款原因
 *
 * @author zengzhangni
 * @date 2019/4/10
 */
@Getter
public enum RefundReasonEnum {
    /**
     * 退款原因 1:包装损坏 2:商品质量问题 3:未按约定时间送达
     */
    PACKING_DAMAGE(1, "包装损坏"),
    QUALITY_PROBLEM(2, "商品质量问题"),
    NOT_AGREED_TIME(3, "未按约定时间送达");

    private final Integer status;
    private final String desc;

    public static String parse(Integer status) {
        for (RefundReasonEnum item : values()) {
            if (status.compareTo(item.getStatus()) == 0) {
                return item.desc;
            }
        }
        return "未知退款原因";
    }

    public static RefundReasonEnum getByValue(Integer status) {
        for (RefundReasonEnum refundReasonEnum : values()) {
            if (refundReasonEnum.status.equals(status)) {
                return refundReasonEnum;
            }
        }
        return null;
    }

    RefundReasonEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
