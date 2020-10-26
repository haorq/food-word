package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单计算商品信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("订单计算商品信息——微信端展示")
public class OrdersCalculateGoodsInfoDTO {
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品类型（1--普通商品，2--秒杀商品；3--团购商品；）")
    private String goodsType;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("商品原价")
    private BigDecimal storePrice;
    @ApiModelProperty("商品销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("商品总原价")
    private BigDecimal goodsStorePrice;
    @ApiModelProperty("优惠前商品金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠后商品金额")
    private BigDecimal discountLaterFee;
    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    private BigDecimal packPrice;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("商品标签")
    private String goodsLabelName;
    @ApiModelProperty("是否赠品（0：不是赠品[默认]；1：是赠品）")
    private Boolean gifts;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
}
