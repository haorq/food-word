package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.query.OrdersListBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单列表信息——后台端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息——后台端")
public class OrdersListAdminDTO extends OrdersListBaseDTO {
    @ApiModelProperty("店铺Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;
    @ApiModelProperty("店铺名称")
    private String storeName;
    @ApiModelProperty("商家编码")
    private String shopCode;
    @ApiModelProperty("所属品牌")
    private String merchantName;
    @ApiModelProperty("订单服务方式。1:外卖配送，2:到店自取，3:堂食正餐，4:堂食快餐，5:堂食外带")
    private Integer deliveryWay;
    @ApiModelProperty("下单方式。1：外卖小程序，2：堂食点餐小程序")
    private Integer orderSource;
    @ApiModelProperty("下单类型。1：外卖，2：堂食")
    private Integer orderWay;
    @ApiModelProperty("商品信息")
    List<OrdersListGoodsAdminDTO> goodsInfo;
    @ApiModelProperty("自提点")
    private String takeAddress;
    @ApiModelProperty("收货人姓名")
    private String consigneeName;
    @ApiModelProperty("收货人手机")
    private String consigneePhone;
    @ApiModelProperty("餐具数量")
    private Integer tableware;
    @ApiModelProperty("支付方式（ 1：余额支付；2：微信支付（WX)； 3：支付宝支付(ALP)；4：POS刷卡支付(YH)；）")
    private Integer payWay;
    @ApiModelProperty("支付时间")
    private String paidTime;
    @ApiModelProperty("自提时间")
    private String actualTime;
}
