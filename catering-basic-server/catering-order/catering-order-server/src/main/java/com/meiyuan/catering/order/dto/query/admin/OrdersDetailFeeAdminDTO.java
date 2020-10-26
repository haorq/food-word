package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情费用信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情费用信息——后台")
public class OrdersDetailFeeAdminDTO {
    @ApiModelProperty("商品原价合计")
    private BigDecimal storePriceTotal;
    @ApiModelProperty("商品现价合计")
    private BigDecimal salesPriceTotal;
    @ApiModelProperty("商品优惠金额")
    private BigDecimal goodsDiscountFee;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("配送费原始价格")
    private BigDecimal deliveryPriceOriginal;
    @ApiModelProperty("配送费满减标准")
    private BigDecimal deliveryPriceFree;
    @ApiModelProperty("实付金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("应付金额")
    private BigDecimal shouldPayAmount;
    @ApiModelProperty("优惠前金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("包装费合计")
    private BigDecimal packPriceTotal;
}
