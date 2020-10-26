package com.meiyuan.catering.order.vo.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/08 15:08
 * @description 营销活动商品出售金额以及订单量统计VO
 **/

@Data
@ApiModel(value = "营销活动商品出售金额以及订单量统计VO")
public class MarketingOrderGoodsCountVo {

    @ApiModelProperty(value = "商品sku")
    private String sku;
    @ApiModelProperty(value = "商品关联订单数量")
    private Integer orderNumCount;
    @ApiModelProperty(value = "商品销售金额")
    private BigDecimal orderBusinessCount;

}
