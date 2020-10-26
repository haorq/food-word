package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 订单验证请求参数——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单验证请求参数——商户")
public class OrdersCheckParamDTO extends MerchantBaseDTO {
    @ApiModelProperty("订单Id")
    private Long orderId;
    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;
}
