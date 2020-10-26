package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mt
 * @date 2020/3/19
 * 自提点活动商家关联表
 **/
@Data
@TableName("catering_marketing_pickup_point_shop_relation")
public class CateringMarketingPickupPointShopRelationEntity extends IdEntity
        implements Serializable {

    /**
     * 自提点活动id
     */
    @TableField(value = "pickup_id")
    private Long pickupId;
    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 赠品数量
     */
    @TableField(value = "gift_quantity")
    private Integer giftQuantity;
    /**
     * 赠品剩余数量
     */
    @TableField(value = "gift_remain_quantity")
    private Integer giftRemainQuantity;

    /**
     * 删除标记
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    private Boolean del;
}
