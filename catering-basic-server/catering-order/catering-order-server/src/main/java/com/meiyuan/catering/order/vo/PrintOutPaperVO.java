package com.meiyuan.catering.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("待打印的外卖小票数据 v1.4.0")
public class PrintOutPaperVO {

    @ApiModelProperty(value = "小票类型")
    private int type;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "预计送达时间")
    private String estimateTimeStr;

    @ApiModelProperty(value = "小票类型字符串")
    private String typeStr;

    @ApiModelProperty(value = "收货人")
    private String consigneeName;

    @ApiModelProperty(value = "收货人电话")
    private String consigneePhone;

    @ApiModelProperty(value = "收货地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "菜品列表")
    private List<PrintOutPaperGoodsVO> ordersGoods;

    @ApiModelProperty(value = "商品总额")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "配送费")
    private BigDecimal deliveryPrice;

    @ApiModelProperty(value = "包装费")
    private BigDecimal packPrice;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountFee;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "下单时间")
    private String billingTime;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "自提点名称")
    private String takeAddress;

    @ApiModelProperty(value = "打印时间")
    private String printTime;


    private String estimateTime;
    private String estimateEndTime;
    private int deliveryWay;

}
