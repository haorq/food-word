package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单计算商品优惠信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("订单计算商品优惠信息——微信")
public class OrdersCalculateGoodsDiscountDTO {
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("订单优惠信息表ID")
    private Long orderDiscountsId;
    @ApiModelProperty("优惠折扣比例（折扣详细数值，如85折为0.85）")
    private BigDecimal discountRate;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("优惠前商品单价")
    private BigDecimal salesPrice;
    @ApiModelProperty("优惠后商品单价")
    private BigDecimal transactionPrice;
    @ApiModelProperty("优惠前商品金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠后商品金额")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("优惠限制级别")
    private Integer limitLevel;
}
