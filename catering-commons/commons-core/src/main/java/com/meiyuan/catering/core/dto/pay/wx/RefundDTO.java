package com.meiyuan.catering.core.dto.pay.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 申请退款参数
 *
 * @author zengzhangni
 * @date 2020-03-25
 */
@Data
@ApiModel("申请退款DTO")
public class RefundDTO implements Serializable {

    @ApiModelProperty("订单id")
    @NotBlank
    private String orderId;
    @ApiModelProperty("货物状态 1:已收到货 2:未收到货")
    private Integer cargoStatus;
    @ApiModelProperty("退款原因 1:包装损坏 2:商品质量问题 3:未按约定时间送达")
    private List<Integer> refundReason;
    @ApiModelProperty("退款说明")
    private String refundRemark;
    @ApiModelProperty("退款凭证(图片地址,最多3张)")
    @Size(max = 3)
    private List<String> refundEvidence;
}
