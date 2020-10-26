package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 今日营业情况——商户端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("今日营业情况——商户端")
public class OrdersCountMerchantDTO {

    @ApiModelProperty("应收")
    private BigDecimal receivableAmount;
    @ApiModelProperty("应收（昨日）")
    private BigDecimal receivableAmountYesterday;
    @ApiModelProperty("实收")
    private BigDecimal actualAmount;
    @ApiModelProperty("实收（昨日）")
    private BigDecimal actualAmountYesterday;
    @ApiModelProperty("订单数")
    private Integer orderTotal;
    @ApiModelProperty("订单数（昨日）")
    private Integer orderTotalYesterday;
    @ApiModelProperty("退款订单数")
    private Integer refundOrderTotal;
    @ApiModelProperty("今日待核销商品数")
    private Integer todayGoodsTotal;
    @ApiModelProperty("明日待核销商品数")
    private Integer tomorrowGoodsTotal;

    public OrdersCountMerchantDTO() {
    }

    public OrdersCountMerchantDTO(BigDecimal receivableAmount, BigDecimal receivableAmountYesterday, BigDecimal actualAmount, BigDecimal actualAmountYesterday) {
        this.receivableAmount = receivableAmount;
        this.receivableAmountYesterday = receivableAmountYesterday;
        this.actualAmount = actualAmount;
        this.actualAmountYesterday = actualAmountYesterday;
    }

    public static OrdersCountMerchantDTO build() {
        return new OrdersCountMerchantDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
