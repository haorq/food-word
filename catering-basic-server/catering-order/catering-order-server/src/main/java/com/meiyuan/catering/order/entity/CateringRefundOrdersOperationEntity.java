package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020-03-31
 */
@Data
@TableName("catering_refund_orders_operation")
public class CateringRefundOrdersOperationEntity extends IdEntity {

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 退款单id
     */
    private Long refundOrderId;
    /**
     * 退款进度  1:退款申请已提交 2:审核通过 3:订单已退款 4:审核拒绝
     */
    private Integer refundOperation;
    /**
     * 说明
     */
    private String remarks;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
