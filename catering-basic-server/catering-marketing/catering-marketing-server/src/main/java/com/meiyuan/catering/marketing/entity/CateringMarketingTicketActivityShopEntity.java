package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName CateringMarketingTicketActivityShopEntity
 * @Description 营销优惠券活动门店关联表
 * @Author gz
 * @Date 2020/8/4 14:14
 * @Version 1.3.0
 */
@Data
@TableName(value = "catering_marketing_ticket_activity_shop")
public class CateringMarketingTicketActivityShopEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 800204840990584492L;
    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;
    /**
     * 门店ID
     */
    @TableField(value = "shop_id")
    private Long shopId;
    /**
     * 门店优惠券状态 :1-下架；2-上架
     */
    @TableField(value = "shop_ticket_status")
    private Integer shopTicketStatus;
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
