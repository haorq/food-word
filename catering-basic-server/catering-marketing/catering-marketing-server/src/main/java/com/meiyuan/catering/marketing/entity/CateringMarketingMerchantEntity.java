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
 * @author luohuan
 * @date 2020/3/24
 * 商家与活动的关联实体类
 **/
@Data
@TableName("catering_marketing_merchant")
public class CateringMarketingMerchantEntity extends IdEntity
        implements Serializable {
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    private String merchantName;
    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;
    /**
     * 店铺名称
     */
    @TableField(value = "shop_name")
    private String shopName;
    /**
     * 关联ID
     */
    @TableField(value = "of_id")
    private Long ofId;

    /**
     * 关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    @TableField(value = "of_type")
    private Integer ofType;

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
