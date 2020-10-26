package com.meiyuan.catering.order.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 订单商品优惠详情表(CateringOrdersGoodsDiscount)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_goods_discount")
public class CateringOrdersGoodsDiscountEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 507740906032771197L;

    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
    /** 订单商品表ID */
    private Long orderGoodsId;
    /** 订单优惠信息表ID */
    private Long orderDiscountsId;
    /** 优惠折扣比例（折扣详细数值，如85折为0.85） */
    private BigDecimal discountRate;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 优惠前商品单价 */
    private BigDecimal salesPrice;
    /** 优惠后商品单价 */
    private BigDecimal transactionPrice;
    /** 优惠前商品金额 */
    private BigDecimal discountBeforeFee;
    /** 优惠后商品金额 */
    private BigDecimal discountLaterFee;
    /** 优惠限制级别（1：表示全部商品或全部服务类型，2：表示指定商品分组或者服务分组，3：表示具体的商品或服务）（目前主要为记录商品在会员卡折扣优惠计算时，商品在哪个系列取得最优折扣） */
    private Integer limitLevel;

}
