package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  订单活动表(CateringOrdersActivity)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-26 18:04:06
 */
@Data
@TableName("catering_orders_activity")
public class CateringOrdersActivityEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = -46028568534655719L;

    /** 关联维度（1：订单维度关联；2：订单商品维度关联） */
    private Integer relationDimension;
    /** 关联ID（根据维度区分是订单表主键ID还是订单商品表主键ID） */
    private Long relationId;
    /** 营销活动类型（1：秒杀；2：团购；3：拼单） */
    private Integer activityType;
    /** 营销活动ID */
    private Long activityId;
    /** 营销活动编号 */
    private String activityNo;
    /** 营销活动名称 */
    private String activityName;
    /** 营销活动是否限制时间（1：有时间限制；0：无时间限制） */
    private Integer activityTimeLimited;
    /** 营销活动开始时间 */
    private LocalDateTime activityBeginTime;
    /** 营销活动截止时间 */
    private LocalDateTime activityEndTime;
    /** 是否删除（0：未删除[默认]；1：已删除） */
    @TableField("is_del")
    private Boolean del;
    /** 创建人ID */
    private Long createBy;
    /** 创建人名称 */
    private String createName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人ID */
    private Long updateBy;
    /** 更新人名称 */
    private String updateName;
    /** 更新时间 */
    private LocalDateTime updateTime;

}
