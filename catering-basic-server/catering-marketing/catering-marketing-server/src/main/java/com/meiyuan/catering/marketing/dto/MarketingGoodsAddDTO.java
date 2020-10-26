package com.meiyuan.catering.marketing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName MarketingGoodsAddDTO
 * @Description 促销商品新增DTO
 * @Author gz
 * @Date 2020/3/16 13:36
 * @Version 1.1
 */
@Data
public class MarketingGoodsAddDTO {
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    private String sku;
    /**
     * 商品数量/发行数量
     */
    @ApiModelProperty(value = "商品数量/发行数量")
    private Integer quantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitQuantity;
    /**
     * 起购数量
     */
    @ApiModelProperty(value = "起购数量")
    private Integer minQuantity;
    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "门店价")
    private BigDecimal storePrice;
}
