package com.meiyuan.catering.marketing.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 18:09
 * @description 营销特价商品活动效果商品列表信息VO
 **/

@Data
@ApiModel(value = "营销特价商品活动效果商品列表信息VO")
public class MarketingSpecialEffectGoodsVO {

    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品sku编码")
    private String skuCode;
    @ApiModelProperty(value = "商品SKU编码值")
    private String propertyValue;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("商品单位，例如件、盒")
    private String unit;
    @ApiModelProperty("销售价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "折扣")
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "限优惠份数")
    private Integer discountLimit;
    @ApiModelProperty(value = "关联订单量")
    private Integer relationOrderNum;
    @ApiModelProperty(value = "销售数量")
    private Integer soldOutQuantity;
    @ApiModelProperty(value = "销售额")
    private BigDecimal business;

}
