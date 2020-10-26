/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.dto.submit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单优惠信息创建Dto
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@Slf4j
public class OrdersDiscountsCreateDTO implements Serializable {

    /** 商户ID */
    private Long id;
    /** 商户ID */
    private Long merchantId;
    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
    /** 优惠类型（1：优惠券；2：促销；3：会员卡；4：积分） */
    private Integer discountType;
    /** 优惠源ID（根据类型不同区分） */
    private Long discountId;
    /** 优惠源编号 */
    private String discountNumber;
    /** 优惠源名称 */
    private String discountName;
    /** 优惠折扣比例（折扣详细数值，如85折为0.85） */
    private BigDecimal discountRate;
    /** 优惠使用积分 */
    private Integer discountScore;
    /** 优惠金额 */
    private BigDecimal discountAmount;
   /** 使用条件：1：订单优惠；2：商品优惠 */
    private Integer usefulCondition;
    /** 描述 */
    private String discountDescribe;
    /** 备注 */
    private String remarks;
    /**
     * 优惠商品均摊信息
     */
    private List<OrdersGoodsDiscountCreateDTO> goodsDiscountList;

}
