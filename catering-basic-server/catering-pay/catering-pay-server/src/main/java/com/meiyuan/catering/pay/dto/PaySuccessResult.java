package com.meiyuan.catering.pay.dto;

import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/10/16 11:15
 * @since v1.5.0
 */
@Data
public class PaySuccessResult {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否首单
     */
    private Boolean firstOrder;

    public PaySuccessResult() {
    }

    public PaySuccessResult(Long orderId, Long userId, Boolean firstOrder) {
        this.orderId = orderId;
        this.userId = userId;
        this.firstOrder = firstOrder;
    }
}
