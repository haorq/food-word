package com.meiyuan.catering.marketing.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/08 15:09
 * @description 商户PC端修改销售菜单，返回的重新设置特价信息的营销特价商品活动商品相关信息
 **/

@Data
@ApiModel(value = "商户PC端修改销售菜单，返回的重新设置特价信息的营销特价商品活动商品相关信息")
public class MarketingPcMenuUpdateSpecialReturnDTO {

    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer fixType;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "SKU编码")
    private String skuCode;

    @ApiModelProperty(value = "SKU值")
    private String propertyValue;

    @ApiModelProperty(value = "折扣")
    private BigDecimal specialNumber;

    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "限优惠份数")
    private Integer discountLimit;

    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

}
