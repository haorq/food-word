package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单详情收货信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情收货信息——后台")
public class OrdersDetailDeliveryAdminDTO {
    @ApiModelProperty("店铺名称")
    private String storeName;
    @ApiModelProperty("收货人姓名")
    private String consigneeName;
    @ApiModelProperty("收货人电话")
    private String consigneePhone;
    @ApiModelProperty("用户属性（1：企业用户；2：个人用户）")
    private Integer memberType;
    @ApiModelProperty("餐具数量")
    private Integer tableware;
    @ApiModelProperty("收货地区")
    private String consigneeArea;
    @ApiModelProperty("详细地址（门牌号）")
    private String consigneeAddress;
}
