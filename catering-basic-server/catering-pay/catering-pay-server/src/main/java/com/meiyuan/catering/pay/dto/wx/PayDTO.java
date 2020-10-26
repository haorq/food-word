package com.meiyuan.catering.pay.dto.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付参数
 *
 * @author zengzhangni
 * @date 2020-03-25
 */
@Data
@ApiModel("支付DTO")
public class PayDTO implements Serializable {

    @ApiModelProperty("操作类型：1：支付，2：取消")
    private Integer type;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("余额支付密码")
    private String password;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("系统交易流水编号")
    private String tradingFlow;

}
