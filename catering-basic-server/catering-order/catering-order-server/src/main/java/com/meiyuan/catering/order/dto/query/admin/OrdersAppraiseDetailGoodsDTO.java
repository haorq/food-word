package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详情商品信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("门店评价商品信息——后台")
public class OrdersAppraiseDetailGoodsDTO implements Serializable {
    @ApiModelProperty("订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderGoodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("成交单价")
    private BigDecimal transactionPrice;
}
