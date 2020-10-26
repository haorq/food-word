package com.meiyuan.catering.order.dto.query.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品信息——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单商品信息——商户")
public class OrdersGoodsMerchantDTO {
    @ApiModelProperty("订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品类型（1--普通商品，2--秒杀商品，3--团购商品，4--特价商品）")
    private Integer goodsType;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("商品规格信息1：单规格，2：多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("商品原价")
    private BigDecimal storePrice;
    @ApiModelProperty("成交单价")
    private BigDecimal transactionPrice;
    @ApiModelProperty("是否是赠品")
    private Boolean gifts;
}
