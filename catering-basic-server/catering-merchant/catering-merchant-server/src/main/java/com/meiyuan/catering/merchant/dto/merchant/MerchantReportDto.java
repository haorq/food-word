package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fql
 */
@Setter
@Getter
public class MerchantReportDto {
    @ApiModelProperty(value = "订单id")
    private Long id;
    @ApiModelProperty(value = "商户id")
    private Long merchantId;
    @ApiModelProperty(value = "优惠前金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty(value = "已支付金额")
    private BigDecimal paidAmount;
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountFee;
    @ApiModelProperty(value = "其他优惠金额")
    private BigDecimal otherDiscountFee;
    @ApiModelProperty(value = "配送优惠")
    private BigDecimal deliveryDiscounts;
    @ApiModelProperty(value = "订单服务方式")
    private Integer deliveryWay;
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime paidTime;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "订单更新人名称")
    private String updateName;
    @ApiModelProperty(value = "订单操作人类型")
    private Integer operationType;
    @ApiModelProperty(value = "订单类型")
    private Integer orderType;
    @ApiModelProperty(value = "商品优惠金额")
    private BigDecimal goodsDiscount;
    @ApiModelProperty(value = "商品类型（1--普通商品，2--秒杀商品；3--团购商品；4--特价）")
    private Integer goodsType;
    @ApiModelProperty(value = "订单更新时间")
    private LocalDateTime updateTime;
}
