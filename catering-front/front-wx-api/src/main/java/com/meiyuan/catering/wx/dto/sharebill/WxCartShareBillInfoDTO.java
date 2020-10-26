package com.meiyuan.catering.wx.dto.sharebill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillGoodsDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaoozu
 * @description 拼单信息
 * @date 2020/3/2511:55
 * @since v1.0.0
 */
@Data
@ApiModel("拼单信息DTO")
public class WxCartShareBillInfoDTO{
    @ApiModelProperty("拼单号")
    private String shareBillNo;
    @ApiModelProperty("发起人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shareUserId;
    @ApiModelProperty("商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;
    @ApiModelProperty("门店id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;
    @ApiModelProperty(value = "订单ID",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @ApiModelProperty("菜单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    @ApiModelProperty(value = "状态：1--选购中，2--结算中,3--已结算",hidden = true)
    private Integer status;
    @ApiModelProperty(value = "状态：1--完成选购，2--提交订单,3--完成拼单")
    private Integer billStatus;
    @ApiModelProperty("拼单类别:1--菜单，2--商品")
    private Integer type;

    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountFee;
    @ApiModelProperty("总包装费")
    private BigDecimal totalPackAmt;
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取,3：全部）")
    private Integer deliveryWay;
    @ApiModelProperty("下单店铺名称")
    private String storeName;

    @ApiModelProperty(value = "拼单人",hidden = true)
    private List<WxCartShareBillUserDTO> userList;

    @ApiModelProperty("拼单商品列表")
    private List<CartShareBillGoodsDTO> shareBillGoodsList;
    @ApiModelProperty("赠品列表")
    List<CartGoodsDTO> giftList;
}
