package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName CateringMarketingTicketActivityEntity
 * @Description 营销优惠券活动主表
 * @Author gz
 * @Date 2020/8/4 14:14
 * @Version 1.3.0
 */
@Data
@TableName(value = "catering_marketing_ticket_activity")
public class CateringMarketingTicketActivityEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 800204820990584498L;
    /**
     * 商户ID、品牌ID
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    @TableField(value = "p_activity_id")
    private Long pActivityId;
    /**
     * 活动名称
     */
    @TableField(value = "activity_name")
    private String activityName;
    /**
     * 活动开始时间
     */
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;
    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;
    /**
     * 生效周期：0-表示每天；其他表示具体的星期
     */
    @TableField(value = "effective_date")
    private String effectiveData;
    /**
     * 活动目的
     */
    @TableField(value = "target")
    private String target;
    /**
     * 目标拉新用户数
     */
    @TableField(value = "target_member",updateStrategy = FieldStrategy.IGNORED)
    private Integer targetMember;
    /**
     * 目标增长营业额
     */
    @TableField(value = "target_turnover",updateStrategy = FieldStrategy.IGNORED)
    private Integer targetTurnover;
    /**
     * 上下架状态
     */
    @TableField(value = "up_down_status")
    private Integer upDownStatus;
    /**
     * 活动类型：1-店内领券；2-店外发券；3-平台补贴
     */
    @TableField(value = "activity_type")
    private Integer activityType;
    /**
     * 活动来源：1-平台；2-品牌
     */
    @TableField(value = "source")
    private Integer source;
    /**
     * 删除标识
     */
    @TableField(value = "is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;
    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;


}
