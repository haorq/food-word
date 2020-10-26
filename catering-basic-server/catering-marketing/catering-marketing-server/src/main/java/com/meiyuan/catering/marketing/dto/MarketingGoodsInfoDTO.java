package com.meiyuan.catering.marketing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @ClassName MarketingGoodsInfoDTO
 * @Description 商品详情DTO
 * @Author gz
 * @Date 2020/3/19 10:44
 * @Version 1.1
 */
@Data
public class MarketingGoodsInfoDTO {
    private Long id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    private Integer ofType;

    @ApiModelProperty(value = "商品编号")
    private String spuCode;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
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
    @ApiModelProperty(value = "起购数量")
    private Integer minQuantity;

    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal salesPrice;
    /**
     * 商品总库存
     */
    @ApiModelProperty(value = "商品总库存")
    private Integer totalQuantity;
    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty(value = "商品分类名称")
    private String goodsCategoryName;
    @ApiModelProperty(value = "餐盒费")
    private BigDecimal packPrice;


    private String goodsDesc;

    private String goodsSynopsis;

    private Boolean changeGoodsPrice;

    private Long shopId;

    private Integer minGroupQuantity;
    private String goodsLabel;
    private Integer goodsStatus;
    private Integer goodsSort;
    private Long merchantId;
}
