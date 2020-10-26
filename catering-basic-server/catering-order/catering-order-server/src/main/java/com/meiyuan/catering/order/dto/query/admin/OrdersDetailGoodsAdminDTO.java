package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单详情商品信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情商品信息——后台")
public class OrdersDetailGoodsAdminDTO {
    @ApiModelProperty("订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderGoodsId;
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("商品类型（1--普通商品，2--秒杀商品；3--团购商品；4--特价）")
    private Integer goodsType;
    @ApiModelProperty("原价")
    private BigDecimal storePrice;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("优惠前商品金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠后商品金额")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("是否赠品（false：不是赠品[默认]；true：是赠品）")
    private Boolean gifts;
    @ApiModelProperty("每单限X份优惠")
    private Integer discountLimit;
    @ApiModelProperty("商品实际金额（分摊优惠后的金额）")
    private BigDecimal actualPrice;
}
