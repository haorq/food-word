package com.meiyuan.catering.core.dto.pay;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zengzhangni
 * @date 2020/5/19 16:29
 * @since v1.1.0
 */
@Data
public class ChargeOrder extends IdEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 充值活动ID
     */
    private Long rechargeRuleId;
    /**
     * 充值订单号
     */
    private String rechargeNo;
    /**
     * 充值流水号
     */
    private String rechargeTraceNo;
    /**
     * 实收金额
     */
    private BigDecimal receivedAmount;
    /**
     * 应收金额
     */
    private BigDecimal payableAmount;
    /**
     * 优化金额
     */
    private BigDecimal discountAmount;
    /**
     * 现金券
     */
    private BigDecimal cashCoupon;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 订单状态，1--待支付，2--支付成功，3--取消
     */
    private Integer status;
    /**
     * 是否有优惠，0--无，1--现金优化，2--折扣，3--现金折扣
     */
    private Integer type;
    /**
     * 1：企业用户，2：个人用户
     */
    private Integer userType;
    /**
     * 充值着IP
     */
    private String operateIp;
}
