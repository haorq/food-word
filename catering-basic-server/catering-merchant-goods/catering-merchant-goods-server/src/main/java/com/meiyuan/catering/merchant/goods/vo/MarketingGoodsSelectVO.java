package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/10 14:08
 * @description V1.3.0促销活动模块选择商品VO
 **/

@Data
@ApiModel(value = "V1.3.0促销活动模块选择商品VO")
public class MarketingGoodsSelectVO {

    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品SPU编号")
    private String spuCode;
    @ApiModelProperty("商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty("菜品分类名称")
    private String categoryName;
    @ApiModelProperty("商品单位，例如件、盒 V1.3.0")
    private String unit;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty("门店商品SKU信息ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    @ApiModelProperty("商品SKU编码")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("原价")
    private BigDecimal storePrice;
    @ApiModelProperty("销售价(个人价)/现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;

}
