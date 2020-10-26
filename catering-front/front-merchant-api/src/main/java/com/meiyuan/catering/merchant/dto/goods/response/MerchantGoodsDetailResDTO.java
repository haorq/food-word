package com.meiyuan.catering.merchant.dto.goods.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 商品详情
 * @date 2020/3/2117:55
 * @since v1.0.0
 */
@Data
@ApiModel("商品详情DTO")
public class MerchantGoodsDetailResDTO {
    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商品分类ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品详情图片")
    private List<String> infoPicture;

    @ApiModelProperty(value = "原价")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "销售价")
    private BigDecimal salesPrice;

    @ApiModelProperty(value = "企业价")
    private BigDecimal enterprisePrice;

    @ApiModelProperty(value = "商品状态：1--已下架,2--出售中")
    private Integer status;

    @ApiModelProperty(value = "商品描述")
    private String goodsDescribeText;
}
