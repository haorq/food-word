package com.meiyuan.catering.allinpay.model.bean.order;

import lombok.Data;

/**
 * created on 2020/8/18 14:00
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
public class CollectPay {
    /**
     * 相关代收交易的“商户订单号”
     */
    private String bizOrderNo;

    /**
     * 金额，单位：分 部分代付时，可以少于或等于托管代收订单金额
     */
    private Long amount;

    public CollectPay(String bizOrderNo, Long amount) {
        this.bizOrderNo = bizOrderNo;
        this.amount = amount;
    }
}
