package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 功能描述:  优惠卷关联商品
 * @author: XiJie Xie
 * @date: 2020/3/27 15:12
 * @version v 3.5
 */
@Data
@ToString(callSuper = true)
public class DiscountGoodsInfoDTO {
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "活动价")
    private BigDecimal salesPrice;
}
