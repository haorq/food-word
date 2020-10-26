package com.meiyuan.catering.core.dto.es;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/11 15:09
 * @description 门店特价商品信息
 **/

@Data
@ApiModel(value = "门店特价商品信息")
public class MarketingSpecialSku {

    @ApiModelProperty(value = "特价商品活动ID")
    private Long specialId;
    @ApiModelProperty(value = "定价方式 1-统一折扣 2-折扣 3-固定价")
    private Integer fixType;
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "折扣值")
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "活动家")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "限优惠份数")
    private Integer discountLimit;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

}
