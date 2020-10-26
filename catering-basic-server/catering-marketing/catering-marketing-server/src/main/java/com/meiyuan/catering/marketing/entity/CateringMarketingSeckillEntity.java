package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销秒杀(CateringMarketingSeckill)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_seckill")
public class CateringMarketingSeckillEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 800204820990584498L;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 上架/下架
     */
    @TableField(value = "up_down")
    private Integer upDown;
    /**
     * 活动对象：企业、个人
     */
    @TableField(value = "object_limit")
    private Integer objectLimit;
    /**
     * 订单作废时限
     */
    @TableField(value = "order_cancellation_time")
    private Integer orderCancellationTime;
    /**
     * 说明
     */
    @TableField(value = "description", updateStrategy = FieldStrategy.IGNORED)
    private String description;
    /**
     * 数据来源：1-平台；2-商家
     */
    @TableField(value = "source")
    private Integer source;
    /**
     * 活动目的V1.3.0
     */
    @TableField(value = "activity_target", updateStrategy = FieldStrategy.IGNORED)
    private String activityTarget;
    /**
     * 拉新目标V1.3.0
     */
    @TableField(value = "user_target", updateStrategy = FieldStrategy.IGNORED)
    private Integer userTarget;
    /**
     * 增长营业额目标V1.3.0
     */
    @TableField(value = "business_target", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal businessTarget;
    /**
     * 删除标记 0-正常；1-删除
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    @TableLogic
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}