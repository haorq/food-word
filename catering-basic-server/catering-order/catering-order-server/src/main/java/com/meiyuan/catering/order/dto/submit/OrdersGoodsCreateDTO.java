/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.dto.submit;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品创建信息Dto
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
public class OrdersGoodsCreateDTO implements Serializable {

    private static final long serialVersionUID = 3842893186825240818L;
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
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品编号
     */
    private String goodsNumber;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品类型（1--普通商品，2--秒杀商品；3--团购商品；）
     */
    private Integer goodsType;
    /**
     * 商品SPU编码
     */
    private String goodsSpuCode;
    /**
     * 商品SKU编码
     */
    private String goodsSkuCode;
    /**
     * 商品购买数量
     */
    private Integer quantity;
    /**
     * 销售单价
     */
    private BigDecimal salesPrice;
    /**
     * 市场单价
     */
    private BigDecimal storePrice;
    /**
     * 成交单价
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
     * 优惠金额
     */
    private BigDecimal discountFee;
    /**
     * 商品分类ID
     */
    private Long goodsGroupId;
    /**
     * 商品分类名称
     */
    private String goodsGroupName;
    /**
     * 商品规格描述
     */
    private String goodsSpecificationDesc;
    /**
     * 商品标签
     */
    private String goodsLabelName;
    /**
     * 商品图片
     */
    private String goodsPicture;
    /**
     * 是否赠品（0：不是赠品[默认]；1：是赠品）
     */
    private Boolean gifts;
    /**
     * 赠品活动id
     */
    private Long giftsActivityId;
    /**
     * 拼单用户id
     */
    private Long shareBillUserId;
    /**
     * 拼单用户名称
     */
    private String shareBillUserName;
    /**
     * 拼单用户头像
     */
    private String shareBillUserAvatar;
    /**
     * 是否删除（0：未删除[默认]；1：已删除）
     */
    private Boolean del;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 每单限X份优惠
     */
    private Integer discountLimit;
    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;
    /**
     * 餐盒费
     */
    private BigDecimal packPrice;

}
