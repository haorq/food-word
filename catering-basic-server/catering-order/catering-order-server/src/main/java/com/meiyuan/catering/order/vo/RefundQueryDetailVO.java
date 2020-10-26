package com.meiyuan.catering.order.vo;

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
@ApiModel("退款详情结果VO")
public class RefundQueryDetailVO {

    @ApiModelProperty("订单id")
    private String orderId;
    @ApiModelProperty("退款单号")
    private String refundNumber;
    @ApiModelProperty("退款状态（1：待退款；2：退款成功；3退款失败）")
    private Integer refundStatus;
    @ApiModelProperty("商家审核结果 1：待审核，2：通过；3：拒绝")
    private Integer businessAuditStatus;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;
    @ApiModelProperty("申请用户")
    private String memberName;

    @ApiModelProperty("实付金额")
    private BigDecimal receivedAmount;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("退款方式(1:原路退回)")
    private Integer refundWay;
    @ApiModelProperty("退款方式(1:原路退回)")
    private String refundWayDesc;
    @ApiModelProperty("退款原因")
    private Integer refundReason;
    @ApiModelProperty("退款原因")
    private String refundReasonDesc;
    @ApiModelProperty("退款说明")
    private String refundRemark;

    @ApiModelProperty("处理商家")
    private String businessName;
    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;
    @ApiModelProperty("操作备注")
    private String businessRemark;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
