package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lhm
 * @date 2020/7/5
 * @description
 **/
@Data
@ApiModel("商户商品SKU新增/修改 dto")
public class MerchantGoodsSkuDTO implements Serializable {

    @ApiModelProperty("id,商品修改时传入的sku")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("规格名称")
    private String propertyValue;
    @ApiModelProperty("规格值")
    private String skuCode;
    @ApiModelProperty("原价")
    private java.math.BigDecimal marketPrice;
    @ApiModelProperty("现价")
    private java.math.BigDecimal salesPrice= BigDecimal.valueOf(-1L);
    @ApiModelProperty("现价是否null")
    private Boolean nullSalesPrice;
    @ApiModelProperty("企业价")
    private java.math.BigDecimal enterprisePrice;
    @ApiModelProperty("企业价是否null")
    private Boolean nullEnterprisePrice;
    @ApiModelProperty("1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    @ApiModelProperty("商品单位，例如件、盒( 天，月，年)")
    private String unit;
    @ApiModelProperty("每日库存")
    private Integer stock;
    @ApiModelProperty("剩余库存")
    private Integer remainStock;
    @ApiModelProperty("0-false 1-true")
    private Boolean isFullStock;
    @ApiModelProperty("餐盒费")
    private BigDecimal packPrice;
    /**
     * 
     */
    @ApiModelProperty("每单限制优惠")
    private Integer discountLimit;
    @ApiModelProperty(value = "1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;

}
