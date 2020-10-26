package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 退款单操作进度枚举定义
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum RefundOperationEnum {

    /**
     * 1:退款申请已提交 2:审核通过 3:订单已退款 4:审核拒绝 5:订单退款关闭
     */
    SUBMIT(1, "退款申请已提交"),

    PASS(2, "审核通过"),

    REFUNDED(3, "订单已退款"),

    REFUSE(4, "审核拒绝"),

    REFUNDED_CLOSE(5, "订单退款关闭");


    /**
     * 枚举code
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    RefundOperationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
