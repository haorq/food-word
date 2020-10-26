package com.meiyuan.catering.finance.util;

import com.meiyuan.catering.core.dto.pay.ChargeOrder;
import com.meiyuan.catering.finance.entity.CateringUserChargeOrderEntity;
import lombok.Data;

import java.util.Objects;

/**
 * 充值订单状态
 *
 * @author zengzhangni
 * @date 2020/3/28
 */
@Data
public class RechargeOrderUtil {
    /**
     * 待支付
     */
    public static final Integer AWAIT_PAY = 1;
    /**
     * 支付成功
     */
    public static final Integer PAY_SUCCESS = 2;
    /**
     * 取消
     */
    public static final Integer PAY_CANCEL = 3;

    /**
     * 描述: 是否是待支付
     *
     * @param order
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/3/28 10:05
     */
    public static Boolean isAwaitPay(CateringUserChargeOrderEntity order) {
        return Objects.equals(order.getStatus(), AWAIT_PAY);
    }

    /**
     * 描述:是否支付成功
     *
     * @param order
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/3/28 10:07
     */
    public static Boolean isPaySuccess(ChargeOrder order) {
        return Objects.equals(order.getStatus(), PAY_SUCCESS);
    }

}
