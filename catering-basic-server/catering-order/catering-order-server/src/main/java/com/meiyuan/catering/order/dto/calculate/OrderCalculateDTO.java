package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单结算信息DTO——内部调用
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ToString(callSuper = true)
@ApiModel("订单结算信息DTO——内部调用")
public class OrderCalculateDTO extends OrderCalculateParamDTO {

    @ApiModelProperty(value = "门店ID")
    private Long storeId;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "门店图片")
    private String storePicture;
    @ApiModelProperty(value = "商家电话")
    private String merchantPhone;
    @ApiModelProperty(value = "订单类型：1：普通订单，2：团购订单，3：拼单订单")
    private Integer orderType;

    @ApiModelProperty(value = "订单商品总件数")
    private Integer totalQuantity;

    @ApiModelProperty(value = "商品总金额")
    private BigDecimal goodsAmount;

    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    private BigDecimal packPrice;

    @ApiModelProperty(value = "配送费实际金额（达到满减则为0）")
    private BigDecimal deliveryPrice;
    @ApiModelProperty(value = "配送费配置金额")
    private BigDecimal deliveryPriceOriginal;
    @ApiModelProperty(value = "配送费配置金额")
    private BigDecimal deliveryPriceFree;

    @ApiModelProperty(value = "优惠前订单金额")
    private BigDecimal discountBeforeFee;

    @ApiModelProperty(value = "优惠后订单金额")
    private BigDecimal discountLaterFee;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountFee;

    @ApiModelProperty(value = "可参与优惠的订单金额")
    private BigDecimal userDiscountOrderAmount;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "特价商品、活动商品不可折扣的金额")
    private BigDecimal notOrdinaryGoodsDiscountFee;

    @ApiModelProperty(value = "卡路里")
    private Integer calories;

    @ApiModelProperty(value = "订单配送信息")
    private OrdersCalculateDeliveryDTO delivery;

    @ApiModelProperty(value = "订单活动信息")
    private List<OrdersCalculateActivityDTO> activityList;

    @ApiModelProperty(value = "订单赠送商品信息")
    List<OrdersGiftGoodsWxDTO> giftGoodsList;

    @ApiModelProperty(value = "订单商品信息")
    List<OrdersCalculateGoodsDTO> goodsList;

    @ApiModelProperty(value = "订单优惠信息")
    List<OrdersCalculateDiscountDTO> discountList;

    public void addActivityList(OrdersCalculateActivityDTO activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        if (activity != null) {
            this.activityList.add(activity);
        }
    }
}
