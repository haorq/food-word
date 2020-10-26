package com.meiyuan.catering.merchant.goods.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lhm
 * @date 2020/7/7
 * @description
 **/
@Data
@ApiModel("门店修改sku dto")
public class ShopSkuDTO {
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("商品sku")
    private String skuCode;
    @ApiModelProperty("商品规格值")
    private String propertyValue;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("每日剩余库存")
    private Integer remainStock;
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("规格名称")
    private String skuName;

}
