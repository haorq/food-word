package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrintOutPaperGoodsVO {

    @ApiModelProperty(value = "菜名")
    private String goodsName;

    @ApiModelProperty(value = "菜品数量")
    private Integer quantity;

    @ApiModelProperty(value = "原价")
    private BigDecimal discountBeforeFee;

    @ApiModelProperty(value = "现价")
    private BigDecimal discountLaterFee;

    @ApiModelProperty(value = "原价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "现价")
    private BigDecimal transactionPrice;
}
