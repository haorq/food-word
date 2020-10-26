package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 营销库存表(CateringMarketingRepertory)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_repertory")
public class CateringMarketingRepertoryEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = -20731126051868094L;
     /**
     * 关联ID
     */
    @TableField(value = "of_id")
    private Long ofId;
    /**
     * 营销商品表主键id
     */
    @TableField(value = "m_goods_id")
    private Long mGoodsId;
     /**
     * 关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @TableField(value = "of_type")
    private Integer ofType;
    /**
     * 秒杀场次关系id（用于秒杀活动）V1.3.0
     */
    @TableField(value = "seckill_event_id")
    private Long seckillEventId;
    /**
     * 平台活动ID V1.3.0
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 平台活动优惠券规则记录表id V1.3.0
     */
    @TableField(value = "ticket_rule_record_id")
    private Long ticketRuleRecordId;
     /**
     * 总库存
     */
    @TableField(value = "total_inventory")
    private Integer totalInventory;
     /**
     * 剩余库存
     */
    @TableField(value = "residual_inventory")
    private Integer residualInventory;
    /**
     * 已售数量
     */
    @TableField(value = "sold_out")
    private Integer soldOut;
     /**
     * 删除标记
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