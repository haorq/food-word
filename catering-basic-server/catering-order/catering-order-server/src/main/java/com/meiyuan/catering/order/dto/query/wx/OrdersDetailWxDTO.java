package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.calculate.ShareBillCalculateGoodsInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单详情信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情信息——微信")
public class OrdersDetailWxDTO {
    @ApiModelProperty("订单基础信息")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @ApiModelProperty("商户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("门店ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty("拼单号")
    private String shareBillNo;
    @ApiModelProperty("售卖模式 1-菜单 2-商品")
    private Integer sellType;
    @ApiModelProperty("订单基础信息")
    private OrdersDetailBaseWxDTO base;
    @ApiModelProperty("订单商品信息")
    private List<OrdersDetailGoodsWxDTO> goods;
    @ApiModelProperty(value = "拼单订单商品信息")
    private List<ShareBillCalculateGoodsInfoDTO> shareBillGoods;
    @ApiModelProperty("订单收货信息")
    private OrdersDetailDeliveryWxDTO detailDelivery;
    @ApiModelProperty("微信端是否可点击。0：不可点击。1：可点击")
    private Integer isWxShow;
    @ApiModelProperty("门店删除标示")
    private Boolean isShopDel;
}
