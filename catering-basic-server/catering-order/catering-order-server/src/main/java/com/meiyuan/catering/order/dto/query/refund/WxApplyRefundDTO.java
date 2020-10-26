package com.meiyuan.catering.order.dto.query.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/3/19
 */
@Data
@ApiModel("退款列表查询DTO")
public class WxApplyRefundDTO implements Serializable {

    @ApiModelProperty("货物状态(未收到货/已收到货)")
    private String cargoStatus;
    @ApiModelProperty("退款原因")
    private String refundReason;
    @ApiModelProperty("退款说明")
    private String refundRemark;
    @ApiModelProperty("退款凭证(图片地址:最多3张)")
    private List<String> refundEvidence;
    @ApiModelProperty("退款单号")
    private String orderId;
}
