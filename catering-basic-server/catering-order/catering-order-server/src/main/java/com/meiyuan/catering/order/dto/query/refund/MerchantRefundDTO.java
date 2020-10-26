package com.meiyuan.catering.order.dto.query.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
@ApiModel("商户退款DTO")
public class MerchantRefundDTO implements Serializable {

    @ApiModelProperty("退款单id")
    private Long id;
    @ApiModelProperty("拒绝退款原因")
    private String refuseReason;


}
