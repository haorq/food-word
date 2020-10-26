package com.meiyuan.catering.marketing.vo.special;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/09/03 18:09
 * @description 营销特价商品活动效果商品列表统计信息VO
 **/

@Data
@ApiModel(value = "营销特价商品活动效果商品列表统计信息VO")
public class MarketingSpecialEffectGoodsCountVO {

    @ApiModelProperty(value = "商品SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "关联订单量")
    private Integer relationOrderNum;
    @ApiModelProperty(value = "销售数量")
    private Integer soldOutQuantity;
    @ApiModelProperty(value = "销售额")
    private BigDecimal business;

}
