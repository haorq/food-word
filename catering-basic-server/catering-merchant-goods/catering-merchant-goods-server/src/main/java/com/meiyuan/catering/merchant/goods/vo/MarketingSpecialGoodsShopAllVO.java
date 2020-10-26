package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 15:09
 * @description 门店所有的商品信息VO
 **/

@Data
@ApiModel(value = "门店所有的商品信息VO")
public class MarketingSpecialGoodsShopAllVO {

    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("商品分类名称")
    private String categoryName;
    @ApiModelProperty("商品单位，例如件、盒 V1.3.0")
    private String unit;
    @ApiModelProperty("sku编码")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("销售价")
    private BigDecimal marketPrice;

}
