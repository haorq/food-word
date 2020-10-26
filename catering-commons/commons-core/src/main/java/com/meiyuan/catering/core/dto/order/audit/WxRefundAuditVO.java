package com.meiyuan.catering.core.dto.order.audit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/24
 */
@Data
@ApiModel("wx-退款进度结果")
public class WxRefundAuditVO {

    @ApiModelProperty("1:退款申请已提交 2:审核通过 3:订单已退款 4:审核拒绝")
    private Integer refundOperation;
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;
    @ApiModelProperty("备注/说明")
    private String remark;

}
