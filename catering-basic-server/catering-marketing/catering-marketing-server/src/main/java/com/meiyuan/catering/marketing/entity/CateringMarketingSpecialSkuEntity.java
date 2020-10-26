package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/02 18:09
 * @description 营销特价SKU信息表实体
 **/

@Data
@TableName("catering_marketing_special_sku")
public class CateringMarketingSpecialSkuEntity extends IdEntity {

    /**
     * 关联ID
     */
    @TableField(value = "of_id")
    private Long ofId;

    /**
     * 门店ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 门店商品SKU信息ID
     */
    @TableField(value = "shop_sku_id")
    private Long shopSkuId;

    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;

    /**
     * SKU编码
     */
    @TableField(value = "sku_code")
    private String skuCode;

    /**
     * SKU值
     */
    @TableField(value = "property_value")
    private String propertyValue;

    /**
     * 折扣
     */
    @TableField(value = "special_number", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal specialNumber;

    /**
     * 活动价
     */
    @TableField(value = "activity_price", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal activityPrice;

    /**
     * 限优惠份数
     */
    @TableField(value = "discount_limit", updateStrategy = FieldStrategy.IGNORED)
    private Integer discountLimit;

    /**
     * 起售数量
     */
    @TableField(value = "min_quantity", updateStrategy = FieldStrategy.IGNORED)
    private Integer minQuantity;

    /**
     * 是否删除 0-否 1-是
     */
    @TableField(value = "is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private Long updateBy;


}
