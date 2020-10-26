package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销商品表(CateringMarketingGoods)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_goods")
public class CateringMarketingGoodsEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -73268189277941356L;
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
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * Code编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    private String goodsName;
    /**
     * 商品类型
     */
    @TableField(value = "goods_type")
    private Integer goodsType;
    /**
     * 商品sku
     */
    @TableField(value = "sku")
    private String sku;
    /**
     * 商品数量/发行数量
     */
    @TableField(value = "quantity")
    private Integer quantity;
    /**
     * 限购数量
     */
    @TableField(value = "limit_quantity")
    private Integer limitQuantity;
    /**
     * 起购数量
     */
    @TableField(value = "min_quantity")
    private Integer minQuantity;
    /**
     * 活动价
     */
    @TableField(value = "activity_price")
    private BigDecimal activityPrice;
    /**
     * 市场价
     */
    @TableField(value = "store_price")
    private BigDecimal storePrice;

    /**
     * 起团数量
     */
    @TableField(value = "min_group_quantity")
    private Integer minGrouponQuantity;

    /**
     * 商品图片
     */
    @TableField(value = "goods_picture", updateStrategy = FieldStrategy.IGNORED)
    private String goodsPicture;
    /**
     * 商品详情介绍
     */
    @TableField(value = "goods_desc", updateStrategy = FieldStrategy.IGNORED)
    private String goodsDesc;

    /**
     * 商品简介
     */
    @TableField(value = "goods_synopsis", updateStrategy = FieldStrategy.IGNORED)
    private String goodsSynopsis;

    /**
     * 规格类型 1-统一规格 2-多规格
     */
    @TableField(value = "goods_spec_type")
    private Integer goodsSpecType;

    /**
     * 打包费
     */
    @TableField(value = "pack_price", updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal packPrice;

    /**
     * 规格值
     */
    @TableField(value = "sku_value")
    private String skuValue;
    /**
     * 商品标签
     */
    @TableField(value = "goods_label", updateStrategy = FieldStrategy.IGNORED)
    private String goodsLabel;
    /**
     * 上/下架状态（1：下架  2：上架）V1.3.0
     */
    @TableField(value = "goods_status")
    private Integer goodsStatus;
    /**
     * 商品排序序号 V1.3.0
     */
    @TableField(value = "goods_sort")
    private Integer goodsSort;
    /**
     * 商品添加方式 1-平台推送2-商家自创3-门店自创
     */
    @TableField(value = "goods_add_type")
    private Integer goodsAddType;

    /**
     * 商品销售渠道 1.外卖小程序 2:食堂 3.全部
     */
    @TableField(value = "goods_sales_channels")
    private Integer goodsSalesChannels;

    @TableField(value = "merchant_id", fill = FieldFill.INSERT)
    private Long merchantId;
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}