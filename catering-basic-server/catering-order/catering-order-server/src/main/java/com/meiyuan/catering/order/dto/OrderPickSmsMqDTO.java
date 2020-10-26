package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 下单成功距自提1小时短信通知DTO
 * @author Administrator
 */
@Data
@ApiModel
public class OrderPickSmsMqDTO implements Serializable {

    @ApiModelProperty(value = "订单id")
    private Long orderId;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "取餐码")
    private String consigneeCode;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "预计自提时间")
    private LocalDateTime estimateTime;
    @ApiModelProperty(value = "预计自提截止时间")
    private LocalDateTime estimateEndTime;
}
