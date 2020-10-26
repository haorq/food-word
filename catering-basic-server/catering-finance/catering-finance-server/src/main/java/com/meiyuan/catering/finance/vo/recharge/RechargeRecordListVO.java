package com.meiyuan.catering.finance.vo.recharge;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/17
 */
@Data
@ApiModel("充值列表查询VO")
public class RechargeRecordListVO implements Serializable {

    @ApiModelProperty("充值时间")
    private LocalDateTime createTime;
    @ApiModelProperty("充值用户")
    private String userName;
    @ApiModelProperty("1：企业用户，2：个人用户")
    private Integer userType;
    @ApiModelProperty("联系方式")
    private String userPhone;
    @ApiModelProperty("实收金额")
    private BigDecimal receivedAmount;
    @ApiModelProperty("应收金额")
    private BigDecimal payableAmount;
    @ApiModelProperty("优化金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("现金券")
    private BigDecimal cashCoupon;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty("状态，1--正常，2--冲正")
    private Integer status;
    @ApiModelProperty("充值者ip")
    private String operateIp;
    @ApiModelProperty("支付方式(1:余额支付 2:微信支付 3:支付宝支付 4:银行卡支付)")
    private Integer payWay;
    @ApiModelProperty("用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
}
