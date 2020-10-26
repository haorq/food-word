package com.meiyuan.catering.marketing.vo.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 20:09
 * @description 商户APP端营销特价商品详情商品信息VO
 **/

@Data
@ApiModel(value = "商户APP端营销特价商品详情商品信息VO")
public class MerchantSpecialDetailGoodsVO {

    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品sku编码")
    private String skuCode;
    @ApiModelProperty(value = "商品SKU编码值")
    private String propertyValue;
    @ApiModelProperty("商品销售价")
    private BigDecimal marketPrice;
    @ApiModelProperty(value = "折扣")
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "限优惠份数")
    private Integer discountLimit;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

    public Integer getDiscountLimit() {
        if(null == discountLimit || 0 == discountLimit) {
            return -1;
        }
        return discountLimit;
    }
}
