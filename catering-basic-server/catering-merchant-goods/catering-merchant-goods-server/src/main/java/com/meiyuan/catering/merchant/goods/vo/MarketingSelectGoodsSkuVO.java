package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName MarketingSelectGoodsSkuVo
 * @Description 促销活动模块商品sku Vo
 * @Author gz
 * @Date 2020/7/10 14:37
 * @Version 1.2.0
 */
@Data
public class MarketingSelectGoodsSkuVO {
    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("sku编码 新增不传")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("销售价(个人价)/现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;



}
