package com.meiyuan.catering.order.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 退款订单商品表(CateringOrdersGoodsRefund)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_goods_refund")
public class CateringOrdersGoodsRefundEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 531940038463524356L;

    /** 退款表Id */
    private Long refundId;
    /** 退款编号 */
    private String refundNumber;
    /** 订单商品表ID */
    private Long orderGoodsId;
    /** 退款商品数量 */
    private Object number;
    /** 退款商品的售价 */
    private BigDecimal price;

}
