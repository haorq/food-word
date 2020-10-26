package com.meiyuan.catering.order.enums;

import lombok.Getter;

/**
 * 订单操作进度枚举定义1
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/10/28 15:04
 */
@Getter
public enum OrderOperationEnum {

    /**
     * 订单操作类型枚举定义1：订单已提交；2：订单已完成；3：订单已取消，4：订单已退款，5：订单已关闭）
     */
    SUBMIT(1, "订单已提交"),

    DONE(2, "订单已完成"),

    CANCELED(3, "订单已取消"),

    REFUND(4, "订单已退款"),

    OFF(5, "订单已关闭");

    /**
     * 枚举code
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    OrderOperationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String parseCode(int code) {
        for (OrderOperationEnum operationEnum : OrderOperationEnum.values()) {
            if (operationEnum.getCode() == code) {
                return operationEnum.getDesc();
            }
        }
        return "";
    }
}
