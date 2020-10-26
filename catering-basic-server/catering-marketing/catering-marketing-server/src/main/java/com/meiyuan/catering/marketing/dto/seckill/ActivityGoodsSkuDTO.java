package com.meiyuan.catering.marketing.dto.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/20 19:08
 * @description 营销活动商品SKU信息DTO
 **/

@Data
@ApiModel(value = "营销活动商品SKU信息DTO")
public class ActivityGoodsSkuDTO {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品SKU编码")
    private String sku;
    @ApiModelProperty(value = "商品SKU值")
    private String skuValue;

}
