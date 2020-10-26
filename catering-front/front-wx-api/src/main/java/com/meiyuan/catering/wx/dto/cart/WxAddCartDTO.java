package com.meiyuan.catering.wx.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 添加购物车（菜单模式、商品模式、拼单）
 * @date 2020/3/2615:15
 * @since v1.0.0
 */
@Data
@ApiModel("添加购物车（菜单模式、商品模式、拼单）")
public class WxAddCartDTO {
    @ApiModelProperty(value = "用户ID/企业用户ID",hidden = true)
    private Long userId;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    private Integer userType;

    @ApiModelProperty(value = "商户ID",required = true)
    private Long merchantId;
    @ApiModelProperty(value = "商品ID/活动商品ID（秒杀）",required = true)
    private Long goodsId;
    @ApiModelProperty(value = "商品类型  1:普通商品 2:秒杀商品",required = true)
    private Integer goodsType;
    @ApiModelProperty(value = "商品sku编码",required = true)
    private String skuCode;

   @ApiModelProperty(value = "商品货品的数量 加为正整数，减为负整数",required = true)
    private Integer number;
    @ApiModelProperty(value = "购物车类型:1--菜单，2--商品，3--拼单，4--秒杀",required = true)
    private Integer type;

    @ApiModelProperty("菜单ID")
    private Long menuId;
    @ApiModelProperty("拼单号")
    private String shareBillNo;
}
