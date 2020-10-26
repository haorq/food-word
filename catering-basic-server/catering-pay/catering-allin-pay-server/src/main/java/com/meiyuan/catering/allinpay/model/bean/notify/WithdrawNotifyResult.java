package com.meiyuan.catering.allinpay.model.bean.notify;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/10/10 09:10
 * @description 提现申请通知结果
 **/

@Data
public class WithdrawNotifyResult {

    /**
     * 通商云订单号
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 订单金额
     */
    private Long amount;
    /**
     * 订单支付完成时间
     */
    private String payDatetime;
    /**
     * 扩展参数
     */
    private String extendInfo;
    /**
     * “OK”标识支付成功；
     * “pending”表示进行中（中间状态）
     * “error”表示支付失败；
     * 提现在成功和失败时都会通知商户；其他订单只在成功时会通知商户。
     */
    private String status;

}
