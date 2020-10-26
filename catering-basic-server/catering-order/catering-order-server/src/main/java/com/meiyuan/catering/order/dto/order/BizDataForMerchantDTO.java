package com.meiyuan.catering.order.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户APP经营数据
 *
 * @author lh
 */
@Data
public class BizDataForMerchantDTO {
    @ApiModelProperty("订单实收金额")
    private BigDecimal actualTotalPrice;
    @ApiModelProperty("营业额")
    private BigDecimal totalPrice;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountTotalPrice;
    @ApiModelProperty("退款金额")
    private BigDecimal refundTotalPrice;
    @ApiModelProperty("订单数量")
    private Integer orderTotalAmount;
    @ApiModelProperty("退款订单数量")
    private Integer refundOrderTotalAmount;
    @ApiModelProperty("商户退款")
    private BigDecimal refundTotalPriceWithShop;
    @ApiModelProperty("客户退款")
    private BigDecimal refundTotalPriceWithClient;

    public BizDataForMerchantDTO() {
        this.actualTotalPrice = BigDecimal.ZERO;
        this.totalPrice = BigDecimal.ZERO;
        this.discountTotalPrice = BigDecimal.ZERO;
        this.refundTotalPrice = BigDecimal.ZERO;
        this.orderTotalAmount = 0;
        this.refundOrderTotalAmount = 0;
        this.refundTotalPriceWithShop = BigDecimal.ZERO;
        this.refundTotalPriceWithClient = BigDecimal.ZERO;
    }

    public BizDataForMerchantDTO(BigDecimal actualTotalPrice, BigDecimal totalPrice, BigDecimal discountTotalPrice, BigDecimal refundTotalPrice, Integer orderTotalAmount, Integer refundOrderTotalAmount, BigDecimal refundTotalPriceWithShop, BigDecimal refundTotalPriceWithClient) {
        this.actualTotalPrice = actualTotalPrice;
        this.totalPrice = totalPrice;
        this.discountTotalPrice = discountTotalPrice;
        this.refundTotalPrice = refundTotalPrice;
        this.orderTotalAmount = orderTotalAmount;
        this.refundOrderTotalAmount = refundOrderTotalAmount;
        this.refundTotalPriceWithShop = refundTotalPriceWithShop;
        this.refundTotalPriceWithClient = refundTotalPriceWithClient;
    }
}
