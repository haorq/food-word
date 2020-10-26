package com.meiyuan.catering.merchant.goods.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description：商品规格信息
 * @author yy
 * @date 2020/7/13
 */
@Data
@ApiModel("商户商品(SKU)详细信息")
public class MerchantGoodsSkuVO {

    @ApiModelProperty(value = "id")
    @TableField("goods_sku_id")
    private Long goodsSkuId;

    @ApiModelProperty(value = "商品编号")
    @TableField("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "sku编码")
    @TableField("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "规格值")
    @TableField("property_value")
    private String propertyValue;

    @ApiModelProperty(value = "原价")
    @TableField("market_price")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "现价")
    @TableField("sales_price")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "企业价")
    @TableField("enterprise_price")
    private BigDecimal enterprisePrice;

    @ApiModelProperty(value = "打包费")
    @TableField("pack_price")
    private BigDecimal packPrice;

    @ApiModelProperty(value = "每日库存")
    @TableField("stock")
    private Integer stock;
}
