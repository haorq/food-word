package com.meiyuan.catering.merchant.dto.dada;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lh
 */
@ApiModel("订单ID入參")
@Data
public class OrderIdReqDto {
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
}
