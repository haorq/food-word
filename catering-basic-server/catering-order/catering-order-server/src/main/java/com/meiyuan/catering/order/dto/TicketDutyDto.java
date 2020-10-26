package com.meiyuan.catering.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券分配
 *
 * @author lh
 */
@Data
public class TicketDutyDto {
    /**
     * 优惠券总金额
     */
    private BigDecimal totalPrice;
    /**
     * 优惠券平台承担部分
     */
    private BigDecimal totalPriceWithPlat;
    /**
     * 优惠券门店承担部分
     */
    private BigDecimal totalPriceWithShop;
}
