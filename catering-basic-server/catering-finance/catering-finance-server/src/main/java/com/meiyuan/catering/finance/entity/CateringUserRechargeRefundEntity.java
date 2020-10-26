package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户充值退款
 *
 * @author zengzhangni
 * @date 2020-03-16
 */
@Data
@TableName("catering_user_recharge_refund")
public class CateringUserRechargeRefundEntity extends IdEntity {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 退款订单号
     */
    private String refundNo;
    /**
     * 退款金额
     */
    private BigDecimal refundAmt;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 实付充值总金额
     */
    private BigDecimal paidAmt;
    /**
     * 充值总金额(报刊==包含折扣和现金券)
     */
    private BigDecimal rechargeTotalAmt;
    /**
     * 余额消费总金额
     */
    private BigDecimal usedAmt;
    /**
     * 状态，1--申请退款，2--审核成功，3--审核失败，4--取消退款
     */
    private Byte status;
    /**
     * 审批人Id
     */
    private Long applyAdminId;
    /**
     * 审批时间
     */
    private String applyTime;
    /**
     * 审批人IP
     */
    private String applyIp;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
