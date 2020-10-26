package com.meiyuan.catering.core.vo.base;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName ActivityIntegralTicketResultVO
 * @Description 活动优惠券、积分赠送成功通知VO
 * @Author gz
 * @Date 2020/8/13 14:37
 * @Version 1.3.0
 */
@Data
public class ActivityIntegralTicketNotifyVO {
    /**
     * 积分数
     */
    private Integer integral;
    /**
     * 优惠券金额
     */
    private BigDecimal ticketAmount;
}
