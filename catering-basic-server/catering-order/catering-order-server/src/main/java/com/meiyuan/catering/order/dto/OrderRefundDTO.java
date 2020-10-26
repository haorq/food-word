package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fql
 */
@Setter
@Getter
public class OrderRefundDTO {

    @ApiModelProperty("订单表id")
    private Long orderId;
    @ApiModelProperty("退款类型")
    private Integer refundType;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("订单服务方式")
    private Integer deliveryWay;


}
