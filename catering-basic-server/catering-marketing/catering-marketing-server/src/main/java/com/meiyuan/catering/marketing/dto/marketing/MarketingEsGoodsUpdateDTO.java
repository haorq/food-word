package com.meiyuan.catering.marketing.dto.marketing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/29 18:09
 * @description 营销秒杀/团购商品修改DTO
 **/

@Data
public class MarketingEsGoodsUpdateDTO {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "规格编号")
    private String skuCode;
    @ApiModelProperty(value = "规格值")
    private String propertyValue;
    @ApiModelProperty(value = "商品销售渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "打包费")
    private BigDecimal packPrice;

}
