package com.meiyuan.catering.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺自提赠品表(CateringSelfMentionGift)实体类
 *
 * @author wxf
 * @since 2020-03-10 10:13:13
 */
@Data
@TableName("catering_self_mention_gift")
public class CateringSelfMentionGiftEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 311638531454024923L;
     /**
     * 店铺id
     */
     @TableField(value = "shop_id")
    private Long shopId;
    /**
     * 自提送赠品活动id
     */
    @TableField(value = "pickup_id")
    private Long pickupId;
     /**
     * 赠品id
     */
     @TableField(value = "gift_id")
    private Long giftId;
     /**
     * 数量
     */
     @TableField(value = "number")
    private Integer number;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    }