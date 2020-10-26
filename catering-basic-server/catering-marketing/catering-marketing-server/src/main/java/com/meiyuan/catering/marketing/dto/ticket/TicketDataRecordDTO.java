package com.meiyuan.catering.marketing.dto.ticket;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName TicketDataRecordDTO
 * @Description
 * @Author gz
 * @Date 2020/8/14 9:35
 * @Version 1.3.0
 */
@Data
public class TicketDataRecordDTO {
    /**
     * 优惠券ids
     */
    private List<Long> ticketIds;
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

    private Long userId;

}
