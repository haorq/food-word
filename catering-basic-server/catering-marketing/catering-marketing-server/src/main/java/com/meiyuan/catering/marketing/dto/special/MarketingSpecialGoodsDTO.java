package com.meiyuan.catering.marketing.dto.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 09:09
 * @description 营销特价商品信息DTO
 **/

@Data
@ApiModel(value = "营销特价商品信息DTO")
public class MarketingSpecialGoodsDTO {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品sku编码")
    private String skuCode;
    @ApiModelProperty(value = "折扣 当定价方式为‘折扣’时，传入此值")
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "活动价 当定价方式为‘固定价’时，传入此值")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "限优惠份数")
    private Integer discountLimit;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

    @ApiModelProperty(value = "门店商品SKU信息ID", hidden = true)
    private Long shopSkuId;
    @ApiModelProperty(value = "商品SKU编码值", hidden = true)
    private String propertyValue;

}
