package com.meiyuan.catering.merchant.dto.goods.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoozu
 * @description 商品列表
 * @date 2020/3/2117:55
 * @since v1.0.0
 */
@Data
@ApiModel("商品列表DTO")
public class MerchantGoodsListResDTO {
    @ApiModelProperty(value = "商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "原价")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "销售价")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "企业价")
    private BigDecimal enterprisePrice;

    @ApiModelProperty(value = "商品状态：1--已下架,2--出售中")
    private Integer status;
}
