package com.meiyuan.catering.allinpay.model.bean.notify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 描述:订单通知结果
 *
 * @author zengzhangni
 * @date 2020/9/30 14:07
 * @since v1.5.0
 */
@Data
public class OrderNotifyResult {
    /**
     * 通商云订单号
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 原通商云订单号 退款订单该字段才返回
     */
    private String oriOrderNo;
    /**
     * 原商户订单号 退款订单该字段才返回
     */
    private String oriBizOrderNo;
    /**
     * 订单金额 单位：分
     */
    private Long amount;
    /**
     * 订单支付完成时间 通商云订单支付完成时间 yyyy-MM-dd HH:mm:ss
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payDatetime;
    /**
     * 商户系统用户标识，商户系统中 唯一编号。 付款人
     * 若平台返回大 B 的 uuid，对应门户 上设置中“商户号”字段
     */
    private String buyerBizUserId;
    /**
     * 退款去向 退款，必填 1：到账户余额
     * 2：到原支付账户银行卡/微信/支付 宝等
     */
    private Long refundWhereabouts;
    /**
     * 代付去向
     */
    private Long payWhereabouts;
    /**
     * 支付人帐号 微信支付的 openid
     * 支付宝平台的 user_id
     * 刷卡交易：隐藏的卡号,例如 621700****4586
     */
    private String acct;
    /**
     * 借贷标志 刷卡消费交易必传
     * 00-借记卡 01-存折 02-信用卡 03-准贷记卡 04-预付费卡 05-境外卡 99-其他
     */
    private String accttype;
    /**
     * 终端号 终端代码
     */
    private String termno;
    /**
     * 渠道商户号 收银宝商户号
     */
    private String cusid;
    /**
     * 通道交易流水号 支付渠道的交易流水号，微信订单 详情“商户单号”，
     * 支付宝订单详 情“商家订单号 走收银宝渠道-对应收银宝接口指 定 trxid
     */
    private String payInterfaceOutTradeNo;
    /**
     * 交易参考号 取值为收银宝接口指定终端参考号 -termrefnum 且收银宝订单 pos 支付时必填
     */
    private String termrefnum;
    /**
     * 渠道手续费 取值为收银宝接口手续费字段-fee
     */
    private String channelFee;
    /**
     * 渠道交易完成时间 取值为收银宝接口交易完成时间 -paytime 且收银宝订单 pos 支付时必填
     */
    private String channelPaytime;
    /**
     * 通道交易类型 收银宝渠道返回的交易类型对应收
     * 银宝接口字段 trxcode
     * VSP501 微信支付 VSP502 微信支付撤销
     * VSP503 微信支付退款 VSP505 手机 QQ 支付 VSP506 手机 QQ 支付撤销 VSP507 手机 QQ 支付退款
     * VSP511 支付宝支付 VSP512 支付宝支付撤销
     * VSP513 支付宝支付退款 VSP551 银联扫码支付 VSP552 银联扫码撤销 VSP553 银联扫码退货
     */
    private String payInterfacetrxcode;
    /**
     * 收银宝终端流水 收银宝终端的流水号-对应收银宝 接口字段 traceno
     */
    private String traceno;
    /**
     * 扩展参数 接口将原样返回
     */
    private String extendInfo;

    /**
     * 订单是否成功 “OK”标识支付成功；
     * “pending”表示进行中（中间状 态）
     * “error”表示支付失败； 提现在成功和失败时都会通知商 户；其他订单只在成功时会通知商 户
     */
    private String status;

}
