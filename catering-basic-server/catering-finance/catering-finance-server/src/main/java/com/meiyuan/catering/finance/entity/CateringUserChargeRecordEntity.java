package com.meiyuan.catering.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户充值明细
 *
 * @author zengzhangni
 * @date 2020-03-16
 */
@Data
@TableName("catering_user_charge_record")
public class CateringUserChargeRecordEntity extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;
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
     * 支付方式(1:余额支付 2:微信支付 3:支付宝支付 4:银行卡支付)
     */
    private Integer payWay;
    /**
     * 1：企业用户，2：个人用户
     */
    private Integer userType;
    /**
     * 状态，1--正常，2--冲正
     */
    private Integer status;
    /**
     * 充值者ip
     */
    private String operateIp;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
