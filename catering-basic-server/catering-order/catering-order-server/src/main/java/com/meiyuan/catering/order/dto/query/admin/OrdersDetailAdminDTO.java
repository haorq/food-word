package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.order.OrderDiscountForPcDTO;
import com.meiyuan.catering.order.dto.order.OrderRefundDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情信息——后台")
public class OrdersDetailAdminDTO {
    @ApiModelProperty("订单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @ApiModelProperty("订单基础信息")
    private OrdersDetailBaseAdminDTO base;
    @ApiModelProperty("订单商品信息")
    private List<OrdersDetailGoodsAdminDTO> goods;
    @ApiModelProperty("订单详情活动信息")
    private List<OrdersDetailActivityAdminDTO> activity;
    @ApiModelProperty("优惠券优惠金额总和")
    private BigDecimal activityTotalPrice;
    @ApiModelProperty("订单收货信息")
    private OrdersDetailDeliveryAdminDTO detailDelivery;
    @ApiModelProperty("订单费用信息")
    private OrdersDetailFeeAdminDTO fee;
    @ApiModelProperty("订单退款信息")
    private OrderRefundDto refundInfo;
    @ApiModelProperty("订单优惠明细（PC）")
    private OrderDiscountForPcDTO orderDiscount;
}
