/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.dto.submit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品优惠创建信息Dto
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersGoodsDiscountCreateDTO implements Serializable {

    private static final long serialVersionUID = 5732030093058084612L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 订单商品表ID
     */
    private Long orderGoodsId;
    /**
     * 订单优惠信息表ID
     */
    private Long orderDiscountsId;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 优惠折扣比例（折扣详细数值，如85折为0.85）
     */
    private BigDecimal discountRate;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 优惠前商品单价
     */
    private BigDecimal salesPrice;
    /**
     * 优惠后商品单价
     */
    private BigDecimal transactionPrice;
    /**
     * 优惠前商品金额
     */
    private BigDecimal discountBeforeFee;
    /**
     * 优惠后商品金额
     */
    private BigDecimal discountLaterFee;
    /**
     * 计算顺序
     */
    private Integer sort;
    /**
     * 优惠限制级别（1：表示全部商品或全部服务类型，2：表示指定商品分组或者服务分组，3：表示具体的商品或服务）（目前主要为记录商品在会员卡折扣优惠计算时，商品在哪个系列取得最优折扣）
     */
    private Integer limitLevel;
    /**
     * 商品分类ID（目前主要为记录商品在会员卡折扣优惠计算时，商品在哪个系列取得最优折扣）
     */
    private String goodsCategoryId;
    /**
     * 商品分类名称
     */
    private String goodsCategoryName;

}
