package com.meiyuan.catering.merchant.dto.dada;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 取消达达配送单入參
 *
 * @author lh
 */
@ApiModel("取消达达配送单入參")
@Data
public class CancelOrderReqDto {

    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    @ApiModelProperty(value = "取消原因", required = true)
    private Integer cancelReasonId;
    @ApiModelProperty(value = "取消原因选其他时，必传", required = false)
    private String cancelReason;

}
