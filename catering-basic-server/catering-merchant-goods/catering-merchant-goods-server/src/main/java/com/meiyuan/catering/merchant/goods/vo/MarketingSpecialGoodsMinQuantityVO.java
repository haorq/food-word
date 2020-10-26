package com.meiyuan.catering.merchant.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/10 09:09
 * @description 特价商品默认的起售数量VO
 **/

@Data
@ApiModel(value = "特价商品默认的起售数量VO")
public class MarketingSpecialGoodsMinQuantityVO {

    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "起售数量")
    private Integer minQuantity;

}
