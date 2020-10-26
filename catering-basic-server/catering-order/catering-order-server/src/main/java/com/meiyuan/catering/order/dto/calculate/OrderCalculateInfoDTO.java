package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单结算信息DTO——微信端展示
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ToString(callSuper = true)
@ApiModel("订单结算信息DTO——微信端展示")
public class OrderCalculateInfoDTO {

    @ApiModelProperty(value = "订单商品总件数")
    private Integer totalQuantity;

    @ApiModelProperty(value = "配送费实际金额（达到满减则为0）")
    private BigDecimal deliveryPrice;
    @ApiModelProperty(value = "配送费配置金额")
    private BigDecimal deliveryPriceOriginal;
    @ApiModelProperty(value = "配送费满减标准金额")
    private BigDecimal deliveryPriceFree;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountFee;

    @ApiModelProperty(value = "优惠前订单金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty(value = "订单优惠后金额（合计）")
    private BigDecimal discountLaterFee;

    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    private BigDecimal packPrice;

    @ApiModelProperty(value = "订单商品信息")
    List<OrdersCalculateGoodsInfoDTO> goodsList;

    @ApiModelProperty(value = "订单赠送商品信息")
    List<OrdersGiftGoodsWxDTO> giftGoodsList;

    @ApiModelProperty(value = "可用优惠卷列表")
    List<CanUseTicketDTO> canUseTicket;

    @ApiModelProperty(value = "不可用优惠卷列表")
    List<NotCanUseTicketDTO> notCanUseTicket;

    @ApiModelProperty(value = "平台优惠卷列表（可用与否根据属性判断）")
    List<UseTicketDTO> ticketWithPlatform;

    @ApiModelProperty(value = "商家优惠卷列表（可用与否根据属性判断）")
    List<UseTicketDTO> ticketWithShop;


}
