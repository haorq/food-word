package com.meiyuan.catering.order.enums;

import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import lombok.Getter;

/**
 * @author lh
 * @version v1.2.0
 * @desc 不同类型红点消息
 * @date 2020-07-10
 */
@Getter
public enum RedMessageEnum {
    /**
     * 未处理订单
     */
    NEW_ORDER(Values.NEW_ORDER, "未处理订单"),
    /**
     * 未处理退款单
     */
    NEW_REFUND_ORDER(Values.NEW_REFUND_ORDER, "未处理退款单"),
    ;


    private String status;
    private String desc;

    RedMessageEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static RedMessageEnum parse(String status) {
        for (RedMessageEnum type : RedMessageEnum.values()) {
            if (status.equals(type.status)) {
                return type;
            }
        }
        throw new CustomException(ErrorCode.MERCHANT_ENUM_STATUS);
    }

    public static class Values {
        private static final String NEW_ORDER = "newOrder";
        private static final String NEW_REFUND_ORDER = "newRefundOrder";
    }

}
