package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单验证结果——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单验证结果——商户")
public class OrdersCheckDTO {
    @ApiModelProperty("订单Id")
    private Long orderId;
    @ApiModelProperty(value = "验证结果消息", required = true)
    private String msg;

    @ApiModelProperty("用户ID")
    private Long memberId;
    @ApiModelProperty("品牌ID")
    private Long merchantId;
}
