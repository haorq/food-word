package com.meiyuan.catering.order.dto.order;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户PC订单流水出参
 *
 * @author lh
 */
@Data
public class OrderForMerchantPcDTO {

    @ApiModelProperty(value = "订单ID", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("订单类型")
    private Integer deliveryWay;
    @ApiModelProperty("订单类型描述（已转义直接展示）")
    private String deliveryWayDesc;
    @ApiModelProperty("订单状态")
    private Integer orderStatus;
    @ApiModelProperty("订单状态（已转义直接展示）")
    private String orderStatusDesc;
    @ApiModelProperty("订单金额")
    private BigDecimal totalPrice;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountPrice;
    @ApiModelProperty("实收金额")
    private BigDecimal payPrice;
    @ApiModelProperty("门店")
    private String shopName;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
    @ApiModelProperty("下单时间（已转义直接展示）")
    private String billingTimeDesc;

    @ApiModelProperty("订单售后状态")
    private Integer refundStatus;
    @ApiModelProperty("订单售后状态（已转义直接展示）")
    private String refundStatusDesc;

    /**
     * 以下字段为v1.5 查询新增
     */
    @ApiModelProperty("商品总金额")
    private BigDecimal goodsAmount;
    @ApiModelProperty("售后审核状态")
    private Integer refundAuditStatus;
    @ApiModelProperty("售后审核状态（已转义直接展示）")
    private String refundAuditStatusDesc;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    /**
     * 包装费->餐盒费
     */
    @ApiModelProperty("餐盒费")
    private BigDecimal packPrice;
    @ApiModelProperty("支付时间")
    private LocalDateTime paidTime;
    @ApiModelProperty("支付时间（已转义直接展示）")
    private String paidTimeDesc;
    @ApiModelProperty("支付金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("支付流水号")
    private String paidTradeNo;
    @ApiModelProperty("支付方式")
    private Integer payWay;
    @ApiModelProperty("支付方式(转义直接展示)")
    private String payWayDesc;
    @ApiModelProperty("是否申请售后")
    private Boolean afterSales;
    @ApiModelProperty("是否申请售后(转义直接展示)")
    private String afterSalesDesc;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
}
