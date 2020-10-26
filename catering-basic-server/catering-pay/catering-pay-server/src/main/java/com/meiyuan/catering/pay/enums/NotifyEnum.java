package com.meiyuan.catering.pay.enums;


import lombok.Getter;

/**
 * 回调地址常量
 *
 * @author zengzhangni
 * @date 2019/4/2
 */
@Getter
public enum NotifyEnum {
    /**
     * 订单 回调
     */
    ORDER("订单回调", "/wx/pay/notify/allinPayNotify"),
    /**
     * 充值 回调
     */
    RECHARGE("充值回调", "rechargeWxPayNotify"),

    AGENT_PAY("托管代付通知", "/wx/allin/notify/agentPayNotify"),


    SIGN_CONTRACT("会员电子协议签约后台通知地址", "/merchant/allin/notify/signContractNotify"),
    SIGN_CONTRACT_H5("会员电子协议签约前端跳转页面", "/download/index.html#/pages/agency/index"),
    SIGN_CONTRACT_QUERY_H5("会员电子协议签约查询前端跳转页面", "/download/index.html#/pages/agency/plus"),
    ORDER_DELIVERY_STATUS_WITH_DADA("达达订单配送状态回调", ""),
    WITHDRAW_APPLY("提现申请通知", "/wx/allin/notify/withdrawApplyNotify");

    private final String desc;
    private final String notifyUrl;

    NotifyEnum(String desc, String notifyUrl) {
        this.desc = desc;
        this.notifyUrl = notifyUrl;
    }
}
