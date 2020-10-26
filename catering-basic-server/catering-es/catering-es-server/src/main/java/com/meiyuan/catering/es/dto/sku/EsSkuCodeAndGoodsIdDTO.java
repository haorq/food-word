package com.meiyuan.catering.es.dto.sku;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/5/27 11:47
 * @description 简单描述
 **/
@Data
public class EsSkuCodeAndGoodsIdDTO {
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("skuCode")
    private String skuCode;
    @ApiModelProperty("门店id")
    private String shopId;
}
