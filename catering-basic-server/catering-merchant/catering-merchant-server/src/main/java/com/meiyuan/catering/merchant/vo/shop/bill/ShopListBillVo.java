package com.meiyuan.catering.merchant.vo.shop.bill;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("对账报表VO")
public class ShopListBillVo {

    @ApiModelProperty("门店名称")
    private String shopName;

    @ApiModelProperty("品牌名称")
    private String merchantName;

    @ApiModelProperty("有效订单数")
    private Integer orderCount;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("订单实收")
    private BigDecimal orderIncome;

    @ApiModelProperty("平台优惠抵扣")
    private BigDecimal platformDiscount;

    @ApiModelProperty("商家优惠抵扣")
    private BigDecimal merchantDiscount;

    private BigDecimal fee;

    @ApiModelProperty("商家实收")
    private BigDecimal merchantIncome;

    @ApiModelProperty("退款额")
    private BigDecimal refundAmount;

    @ApiModelProperty("退款数")
    private BigDecimal refundCount;

    private String shopId;

    private String merchantId;

    @ApiModelProperty("品牌创建时间")
    private LocalDateTime merchantCreateTime;

    @ApiModelProperty("店铺状态:：true:删除,false:未删除")
    private boolean del;
}
