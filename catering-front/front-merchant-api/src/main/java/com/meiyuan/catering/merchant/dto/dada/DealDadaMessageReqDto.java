package com.meiyuan.catering.merchant.dto.dada;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 处理达达消息入參
 *
 * @author lh
 */
@ApiModel("处理达达消息入參")
@Data
public class DealDadaMessageReqDto {

    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    @ApiModelProperty(value = "达达订单ID", required = false)
    private Integer dadaOrderId;
    @ApiModelProperty(value = "操作结果。1：不同意取消；2：同意取消", required = true)
    private Integer isConfirm;
}
