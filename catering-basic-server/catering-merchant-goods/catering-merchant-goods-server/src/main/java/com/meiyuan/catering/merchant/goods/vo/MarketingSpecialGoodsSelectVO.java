package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 11:09
 * @description 营销特价商品活动创建/编辑商品选择VO
 **/

@Data
@ApiModel(value = "营销特价商品活动创建/编辑商品选择VO")
public class MarketingSpecialGoodsSelectVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品SKU编码")
    private String skuCode;
    @ApiModelProperty("商品SKU规格值")
    private String propertyValue;
    @ApiModelProperty("规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("商品单位，例如件、盒")
    private String unit;
    @ApiModelProperty("商品销售价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "门店商品SKU信息ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopSkuId;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

}
