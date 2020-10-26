package com.meiyuan.catering.order.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单列表信息——父类
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息")
public class OrdersListBaseDTO {
    @ApiModelProperty("订单Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("系统交易流水编号(商户交易单号)")
    private String tradingFlow;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
    @ApiModelProperty("订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer orderStatus;
    @ApiModelProperty("订单状态描述（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private String orderStatusStr;
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;
}
