package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/8 11:33
 */
@Data
@TableName("catering_marketing_rule_ticket_relation")
public class CateringMarketingRuleTicketRelationEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 202008081133110503L;

    /**
     * 活动规则 id
     */
    @TableField(value = "activity_rule_id")
    private Long activityRuleId;

    /**
     * 优惠券 id
     */
    @TableField(value = "ticket_id")
    private Long ticketId;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 被分享人优惠券 id
     */
    @TableField(value = "passive_ticket_id")
    private Long passiveTicketId;

    /**
     * 被分享人发放数量
     */
    @TableField(value = "passive_amount")
    private Integer passiveAmount;
}
