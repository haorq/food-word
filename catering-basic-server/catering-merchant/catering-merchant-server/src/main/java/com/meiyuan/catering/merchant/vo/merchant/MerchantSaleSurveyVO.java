package com.meiyuan.catering.merchant.vo.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MerchantSaleSurveyVO {

    @ApiModelProperty("订单实收")
    private BigDecimal orderActualAmount;
    @ApiModelProperty("营业额")
    private BigDecimal turnover;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("订单数")
    private Integer orderNum;
    @ApiModelProperty("退款单")
    private Integer refundOrderNum;

}
