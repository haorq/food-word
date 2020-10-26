package com.meiyuan.catering.merchant.vo.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("对账报表门店详情VO")
public class ShopBillDetailVo implements Serializable {

    @ApiModelProperty("门店名称")
    private String shopName;

    private String shopId;

    private String merchantId;

    private String orderId;

    private String orderNumber;

    @ApiModelProperty("订单总额")
    private BigDecimal orderAmount;

    @ApiModelProperty("订单实收")
    private BigDecimal paidAmount;

    @ApiModelProperty("平台优惠抵扣")
    private BigDecimal platformDiscount;

    @ApiModelProperty("商家优惠抵扣")
    private BigDecimal merchantDiscount;

    @ApiModelProperty("配送费")
    private BigDecimal deliveryFee;

    private BigDecimal deliveryPriceOriginal;

    @ApiModelProperty("订单服务方式。1:外卖配送，2:到店自取，3:堂食正餐，4:堂食快餐，5:堂食外带")
    private int deliveryWay;

    @ApiModelProperty("商家实收")
    private BigDecimal merchantIncome;

    @ApiModelProperty("退款额")
    private BigDecimal refundAmount;

    @ApiModelProperty("订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer orderStatus;

    @ApiModelProperty("支付方式（ 1：余额支付；2：微信支付（WX)； 3：支付宝支付(ALP)；4：POS刷卡支付(YH)；）（为用户在订单完成时最后选择的支付方式）")
    private Integer payWay;

    @ApiModelProperty("支付时间")
    private LocalDateTime paidTime;

    @ApiModelProperty("支付流水号")
    private String tradingFlow;

}
