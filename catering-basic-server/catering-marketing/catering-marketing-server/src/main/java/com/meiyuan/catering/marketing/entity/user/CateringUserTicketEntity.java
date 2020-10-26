package com.meiyuan.catering.marketing.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName CateringUserTicketEntity
 * @Description 用户优惠券实体
 * @Author gz
 * @Date 2020/3/19 17:57
 * @Version 1.1
 */
@Data
@TableName("catering_user_ticket")
public class CateringUserTicketEntity extends IdEntity {
    /** 用户id */
    @TableField("user_id")
    private Long userId;
    /**用户类型 1-企业用户；2-个人用户*/
    @TableField("user_type")
    private Integer userType;
    /** 券id */
    @TableField("ticket_id")
    private Long ticketId;
    @TableField("ticket_activity_id")
    private Long ticketActivityId;
    /**
     * 平台活动优惠券规则记录表id V1.3.0
     */
    @TableField(value = "ticket_rule_record_id")
    private Long ticketRuleRecordId;
    /** 券名称 */
    @TableField("ticket_name")
    private String ticketName;
    /** 订单id：使用后回填 */
    @TableField(value = "order_id",updateStrategy = FieldStrategy.IGNORED)
    private Long orderId;
    /** 领取时间 */
    @TableField(value = "get_time")
    private LocalDateTime getTime;
    /** 使用时间 */
    @TableField(value = "use_time",updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime useTime;
    /** 使用截至时间v1.1.0 */
    @TableField(value = "use_end_time")
    private LocalDateTime useEndTime;
    /** 是否已使用 */
    @TableField("is_used")
    private Boolean used;
    /** 是否已核销 */
    @TableField("is_consume")
    private Boolean consume;
    /** 删除标记 */
    @TableField(value = "is_del")
    private Boolean del;
    @TableField(value ="create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value ="create_by",fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(value ="update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value ="update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}
