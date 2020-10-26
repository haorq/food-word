package com.meiyuan.catering.order.dto.query.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单详情收货信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情收货信息——后台")
public class OrdersDetailDeliveryWxDTO {
    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取）")
    private Integer deliveryWay;
    @ApiModelProperty("自提门店Id")
    private String storeId;
    @ApiModelProperty("自提门店")
    private String storeName;
    @ApiModelProperty("自提点负者人")
    private String storeManager;
    @ApiModelProperty("自提点电话")
    private String storePhone;
    @ApiModelProperty("取餐码/送达码")
    private String consigneeCode;
    @ApiModelProperty("收货人姓名")
    private String consigneeName;
    @ApiModelProperty("收货人性别")
    private String consigneeSex;
    @ApiModelProperty("收货人电话")
    private String consigneePhone;
    @ApiModelProperty("收货地区")
    private String consigneeArea;
    @ApiModelProperty("详细地址")
    private String consigneeAddress;
    @ApiModelProperty("自提、配送时间")
    private String consigneeTime;
    @ApiModelProperty("立即送达时间")
    private String immediateDeliveryTime;
    @ApiModelProperty("自提、配送时间（非格式化）")
    private LocalDateTime estimateTime;
    @ApiModelProperty("实际取餐时间")
    private LocalDateTime actualTime;
}
