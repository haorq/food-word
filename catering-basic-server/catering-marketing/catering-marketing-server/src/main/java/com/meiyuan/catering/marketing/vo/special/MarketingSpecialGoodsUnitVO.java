package com.meiyuan.catering.marketing.vo.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/24 11:09
 * @description 特价商品单位VO
 **/

@Data
@ApiModel(value = "特价商品单位VO")
public class MarketingSpecialGoodsUnitVO  {

    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "单位")
    private String unit;

}
