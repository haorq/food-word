package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName MerchantAppGoodsSkuDTO
 * @Description 商户APP商品SKU
 * @Author gz
 * @Date 2020/7/8 17:10
 * @Version 1.2.0
 */
@Data
@ApiModel("商户APP商品SKU")
public class MerchantAppGoodsSkuDTO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("sku编码")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("现价(个人价) == -1表示不存在现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价 == -1表示不存在企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("起卖数")
    private Integer lowestBuy;
    @ApiModelProperty(value = "剩余库存")
    private Integer remainStock;
    @ApiModelProperty(value = "每单限受X份优惠")
    private Integer discountLimit;
    @ApiModelProperty("1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    @ApiModelProperty("商品单位，例如件、盒( 天，月，年)")
    private String unit;
    @ApiModelProperty("每日库存")
    private Integer stock;
    @ApiModelProperty("0-false 1-true")
    private Boolean isFullStock;

    @ApiModelProperty("餐盒费 v1.5.0")
    private BigDecimal packPrice;

}
