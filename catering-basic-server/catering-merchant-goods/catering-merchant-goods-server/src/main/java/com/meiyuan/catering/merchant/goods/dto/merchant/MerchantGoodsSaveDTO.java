package com.meiyuan.catering.merchant.goods.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yy
 * @date 2020/7/13
 */
@Data
public class MerchantGoodsSaveDTO {

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品编号")
    private String spuCode;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "规格值")
    private String propertyValue;
}
