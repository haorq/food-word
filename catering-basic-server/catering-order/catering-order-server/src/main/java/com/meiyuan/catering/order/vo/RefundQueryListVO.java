package com.meiyuan.catering.order.vo;

import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/19
 */
@Data
@ApiModel("退款列表结果VO")
public class RefundQueryListVO extends IdEntity {

    @ApiModelProperty("退款单号")
    private String refundNumber;
    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;
    @ApiModelProperty("处理商家")
    private String businessName;
    @ApiModelProperty("申请用户")
    private String memberName;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("退款状态（1：待退款；2：退款成功；3退款失败）")
    private Integer refundStatus;
    @ApiModelProperty("商家审核结果 1：待审核，2：通过；3：拒绝")
    private Integer businessAuditStatus;
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
