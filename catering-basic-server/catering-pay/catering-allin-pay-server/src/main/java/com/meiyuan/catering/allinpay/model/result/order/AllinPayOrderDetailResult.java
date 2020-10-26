package com.meiyuan.catering.allinpay.model.result.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/09/25 18:09
 * @description 订单信息查询返回值
 **/

@Data
public class AllinPayOrderDetailResult {

    /**
     * 通商云订单号
     * <p>
     * 必返
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     * <p>
     * 必返
     */
    private String bizOrderNo;
    /**
     * 原通商云订单号	退款订单该字段才返回
     * <p>
     * 非必返
     */
    private Long oriOrderNo;
    /**
     * 原商户订单号	退款订单该字段才返回
     * <p>
     * 非必返
     */
    private String oriBizOrderNo;
    /**
     * 订单状态
     * 1 - 未支付
     * 3 - 交易失败 交易过程中出现错误
     * 4 - 交易成功
     * 5 - 交易成功-发生退款  交易成功，但是发生了退款。
     * 6 - 关闭  未支付的订单，每天日终（00:30）批量关闭已创建未支付，且创建时间大于24小时的订单。
     * 99 - 进行中
     * <p>
     * 必返
     */
    private Long orderStatus;
    /**
     * 失败原因	订单失败时返回
     * <p>
     * 非必返
     */
    private String errorMessage;
    /**
     * 订单金额	单位：分
     * <p>
     * 必返
     */
    private Long amount;
    /**
     * 订单支付完成时间	yyyy-MM-dd HH:mm:ss
     * <p>
     * 非必返
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payDatetime;
    /**
     * 商户系统用户标识，商户系统中唯一编号。付款人
     * 若平台返回大B的uuid，对应门户上设置中“商户号”字段
     * <p>
     * 必返
     */
    private String buyerBizUserId;
    /**
     * 退款去向
     * <p>
     * 1：到账户余额
     * 2：到原支付账户银行卡/微信/支付宝等
     * <p>
     * 非必返
     */
    private Long refundWhereabouts;
    /**
     * 代付去向	代付去向
     * 1：到账户余额
     * <p>
     * 非必返
     */
    private Long payWhereabouts;
    /**
     * 支付人帐号
     * <p>
     * 微信支付的openid
     * 支付宝平台的user_id
     * 刷卡交易：隐藏的卡号,例如621700****4586
     * <p>
     * 非必返
     */
    private String acct;
    /**
     * 借贷标志
     * <p>
     * 刷卡消费交易必传
     * 00-借记卡
     * 01-存折
     * 02-信用卡
     * 03-准贷记卡
     * 04-预付费卡
     * 05-境外卡
     * 99-其他
     * <p>
     * 非必返
     */
    private String accttype;
    /**
     * 终端号	终端代码
     * <p>
     * 非必返
     */
    private String termno;
    /**
     * 渠道商户号	收银宝商户号
     * <p>
     * 非必返
     */
    private String cusid;
    /**
     * 通道交易流水号
     * <p>
     * 支付渠道的交易流水号
     * 微信订单详情“商户单号”
     * 支付宝订单详情“商家订单号”
     * 走收银宝渠道-对应收银宝接口指定trxid
     * <p>
     * 非必返
     */
    private String payInterfaceOutTradeNo;
    /**
     * 交易参考号	取值为收银宝接口指定终端参考号-termrefnum且收银宝订单pos支付时必填
     * <p>
     * 非必返
     */
    private String termrefnum;
    /**
     * 渠道手续费	取值为收银宝接口手续费字段-fee
     * <p>
     * 非必返
     */
    private String channelFee;
    /**
     * 渠道交易完成时间	取值为收银宝接口交易完成时间-paytime且收银宝订单pos支付时必填
     * <p>
     * 非必返
     */
    private String channelPaytime;
    /**
     * 通道交易类型
     * <p>
     * 收银宝渠道返回的交易类型对应收银宝接口字段trxcode
     * VSP501微信支付
     * VSP502微信支付撤销
     * VSP503微信支付退款
     * VSP505手机QQ支付
     * VSP506手机QQ支付撤销
     * VSP507手机QQ支付退款
     * VSP511支付宝支付
     * VSP512支付宝支付撤销
     * VSP513支付宝支付退款
     * VSP551银联扫码支付
     * VSP552银联扫码撤销
     * VSP553银联扫码退货
     * <p>
     * 非必返
     */
    private String payInterfacetrxcode;
    /**
     * 收银宝终端流水	收银宝终端的流水号-对应收银宝接口字段traceno
     * <p>
     * 非必返
     */
    private String traceno;
    /**
     * 扩展参数	订单接口信息原样返回，不可包含“|”特殊字符
     * <p>
     * 非必返
     */
    private String extendInfo;

}
