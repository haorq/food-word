package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单退款表(CateringOrdersRefund)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@Accessors(chain = true)
@TableName("catering_orders_refund")
public class CateringOrdersRefundEntity extends IdEntity {
    private static final long serialVersionUID = 210515488420173935L;

    /**
     * 订单表Id
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 系统交易流水编号
     */
    private String tradingFlow;
    /**
     * 退款编号
     */
    private String refundNumber;
    /**
     * 退款人ID
     */
    private Long memberId;
    /**
     * 退款人名称
     */
    private String memberName;
    /**
     * 退款人电话
     */
    private String memberPhone;
    /**
     * 退款类型(1：系统退款(默认) 2：店长退款 3：运营退款 4：用户退款)
     */
    private Integer refundType;
    /**
     * 退款方式(1:原路退回)
     */
    private Integer refundWay;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 已退金额
     */
    private BigDecimal refundedAmount;
    /**
     * 退款完成时间
     */
    private LocalDateTime refundCompleteTime;
    /**
     * 退款状态（1：待退款；2：退款成功；3退款失败）
     */
    private Integer refundStatus;
    /**
     * 是否删除（0：未删除[默认]；1：已删除）
     */
    @TableLogic
    @TableField("is_del")
    private Boolean del;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 退款凭证
     */
    private String refundEvidence;
}
