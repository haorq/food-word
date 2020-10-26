package com.meiyuan.catering.marketing.dto.activity;

import lombok.Data;

/**
 * @ClassName ActivityRepertoryDTO
 * @Description
 * @Author gz
 * @Date 2020/8/22 9:23
 * @Version 1.3.0
 */
@Data
public class ActivityRepertoryDTO {
    /**
     * 优惠券ID
     */
    private Long ticketId;
    /**
     * 总库存
     */
    private Integer totalInventory;
    /**
     * 活动 id
     */
    private Long activityId;
    /**
     * 平台活动优惠券规则记录表id
     */
    private Long ticketRuleRecordId;
}
