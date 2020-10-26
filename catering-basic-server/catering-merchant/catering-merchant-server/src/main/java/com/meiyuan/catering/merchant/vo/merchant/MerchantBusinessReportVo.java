package com.meiyuan.catering.merchant.vo.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fql
 */
@Setter
@Getter
@ApiModel("查询商户 营业概览")
public class MerchantBusinessReportVo {

    @ApiModelProperty("营业额")
    private BigDecimal turnover;
    @ApiModelProperty("营业额上涨百分比")
    private String percentageUp;
    @ApiModelProperty("外卖营业额")
    private BigDecimal outTurnover;
    @ApiModelProperty("外卖营业额上涨百分比")
    private String outPercentageUp;
    @ApiModelProperty("堂食营业额")
    private BigDecimal innerTurnover;
    @ApiModelProperty("堂食营业额上涨百分比")
    private String innerPercentageUp;

    @ApiModelProperty("优惠金额")
    private BigDecimal totalDiscountAmount;
    @ApiModelProperty("优惠金额上涨百分比")
    private String discountAmountPercentageUp;
    @ApiModelProperty("订单优惠")
    private BigDecimal orderDiscountAmount;
    @ApiModelProperty("订单优惠金额上涨百分比")
    private String orderDiscountAmountPercentageUp;
    @ApiModelProperty("商品优惠")
    private BigDecimal goodsDiscountAmount;
    @ApiModelProperty("商品优惠金额上涨百分比")
    private String goodsDiscountAmountPercentageUp;

    @ApiModelProperty("订单实收")
    private BigDecimal orderActualAmount;
    @ApiModelProperty("订单实收上涨百分比")
    private String orderAmountPercentageUp;
    @ApiModelProperty("外卖订单实收")
    private BigDecimal outOrderAmount;
    @ApiModelProperty("外卖订单实收上涨百分比")
    private String outOrderAmountPercentageUp;
    @ApiModelProperty("堂食订单实收")
    private BigDecimal innerOrderAmount;
    @ApiModelProperty("堂食订单实收上涨百分比")
    private String innerOrderAmountPercentageUp;

    @ApiModelProperty("订单数")
    private Integer orderNum;
    @ApiModelProperty("订单数上涨百分比")
    private String orderNumPercentageUp;
    @ApiModelProperty("外卖订单数")
    private Integer outOrderNum;
    @ApiModelProperty("订单数上涨百分比")
    private String outOrderNumPercentageUp;
    @ApiModelProperty("堂食订单数")
    private Integer innerOrderNum;
    @ApiModelProperty("堂食订单数上涨百分比")
    private String innerOrderNumPercentageUp;


    @ApiModelProperty("退款单")
    private Integer refundOrderNum;
    @ApiModelProperty("退款单数上涨百分比")
    private String refundOrderNumPercentageUp;
    @ApiModelProperty("外卖退款单")
    private Integer outRefundOrderNum;
    @ApiModelProperty("外卖退款单数上涨百分比")
    private String outRefundOrderNumPercentageUp;
    @ApiModelProperty("堂食退款单")
    private Integer innerRefundOrderNum;
    @ApiModelProperty("堂食退款单数上涨百分比")
    private String innerRefundOrderNumPercentageUp;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("退款金额上涨百分比")
    private String refundAmountPercentageUp;
    @ApiModelProperty("商户主动退款金额")
    private BigDecimal merchantRefundAmount;
    @ApiModelProperty("商户主动退款金额上涨百分比")
    private String merchantRefundAmountPercentageUp;
    @ApiModelProperty("顾客售后退款金额")
    private BigDecimal afterSaleRefundAmount;
    @ApiModelProperty("顾客售后退款金额上涨百分比")
    private String afterSaleRefundAmountPercentageUp;

    @ApiModelProperty("时间查询类型,1：自然日；2：营业日；3：本周；4：本月；5：自定义时间段")
    private Integer queryType;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
