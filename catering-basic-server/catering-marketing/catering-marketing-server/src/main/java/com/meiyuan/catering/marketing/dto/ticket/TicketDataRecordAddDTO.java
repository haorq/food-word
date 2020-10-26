package com.meiyuan.catering.marketing.dto.ticket;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName TicketDataRecordAddDTO
 * @Description
 * @Author gz
 * @Date 2020/8/14 9:53
 * @Version 1.3.0
 */
@Data
    public class TicketDataRecordAddDTO {

    private Long id;
    /**
     * 优惠券id
     */
    private Long ticketId;
    /**
     * 品牌id
     */
    private Long merchantId;
    /**
     * 门店id
     */
    private Long shopId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单实付金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单优惠券金额
     */
    private BigDecimal discountBeforeFee;
    /**
     * 当前用户是否新用户
     */
    private Boolean newMember;
    /**
     * 是否品牌新用户
     */
    private Boolean brandNew;

    private Long userId;

    private Boolean orderSuccess;
}
