package com.meiyuan.catering.order.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情商品信息——内部调用
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情商品信息——内部调用")
public class OrdersDetailGoodsDTO {
    @ApiModelProperty("订单商品ID")
    private Long orderGoodsId;
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商品类型 1--普通商品，2--秒杀商品；3--团购商品")
    private Integer goodsType;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("成交单价")
    private BigDecimal transactionPrice;
    @ApiModelProperty("优惠后商品总价")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("餐盒费")
    private BigDecimal packPrice;
    @ApiModelProperty("是否赠品（0：不是赠品[默认]；1：是赠品）")
    private Boolean gifts;
    /** 赠品活动id */
    private Long giftsActivityId;
    /** 商品SKU编码 */
    private String goodsSkuCode;
    private Long shopId;
    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
}
