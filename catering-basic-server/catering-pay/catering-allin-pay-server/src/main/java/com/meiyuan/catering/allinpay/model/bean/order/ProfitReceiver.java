package com.meiyuan.catering.allinpay.model.bean.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * created on 2020/8/17 16:40
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
public class ProfitReceiver {
    /**
     * 分账单号
     */
    private String profitSharingOrderNo;
    /**
     * 唯一用户标识 微信是openid或者微信商户号 通联是业务系统(用户/商户)唯一标识
     */
    private String uniqueIdentity;
    private String accountSetNo;
    /**
     * 金额，单位：分
     */
    private Integer amount;
    /**
     * 手续费
     */
    private Integer fee;
    /**
     * 分账描述
     */
    private String description;

    public ProfitReceiver() {
    }

    public ProfitReceiver(String uniqueIdentity, Integer amount) {
        this.uniqueIdentity = uniqueIdentity;
        this.amount = amount;
    }

    public ProfitReceiver(String uniqueIdentity, BigDecimal amount) {
        this.uniqueIdentity = uniqueIdentity;
        this.amount = amount.multiply(BigDecimal.valueOf(100)).intValue();
    }
}
