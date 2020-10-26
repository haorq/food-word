package com.meiyuan.catering.order.enums;


import lombok.Getter;

/**
 * 退款状态
 *
 * @author zengzhangni
 * @date 2019/3/20
 */
@Getter
public enum RefundStatusEnum {
    /**
     * 退款状态（1：待退款；2：退款成功；3:退款失败/拒绝）
     */
    AWAIT_REFUND(1, "等待审核"),
    SUCCESS_REFUND(2, "审核通过"),
    FAIL_REFUND(3, "审核拒绝");

    private final Integer status;

    private final String desc;

    RefundStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String parse(Integer status) {
        for (RefundStatusEnum item : values()) {
            if (status.compareTo(item.status) == 0) {
                return item.desc;
            }
        }
        return "未知退款状态";
    }
}
