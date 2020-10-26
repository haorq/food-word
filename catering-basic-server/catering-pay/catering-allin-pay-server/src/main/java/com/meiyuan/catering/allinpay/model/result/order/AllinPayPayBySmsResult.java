package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/29 14:09
 * @description 通联确认支付（前台+短信验证码确认）返回值
 *              注：为【充值申请】、【消费申请】、【托管代收申请（标准版+简化校验版）】接口中frontUrl页面跳转返回的数据。
 **/

@Data
public class AllinPayPayBySmsResult {

    /**
     * 通商云订单号
     *
     * 必返
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     *
     * 必返
     */
    private String bizOrderNo;
    /**
     * 订单金额	单位：分
     *
     * 必返
     */
    private Long amount;
    /**
     * 订单支付完成时间
     *
     * 通商云订单支付完成时间yyyy-MM-dd HH:mm:ss
     *
     * 非必返
     */
    private String payDatetime;
    /**
     * 商户系统用户标识，商户系统中唯一编号。付款人
     *
     * 若平台返回大B的uuid，对应门户上设置中“商户号”字段
     *
     * 必返
     */
    private String buyerBizUserId;
    /**
     * 扩展参数
     *
     * 接口将原样返回，不可包含“|”特殊字符
     *
     * 非必返
     */
    private String extendInfo;
    /**
     * 订单是否成功
     *
     * “OK”标识支付成功；
     * “pending”表示进行中（中间状态）
     * “error”表示支付失败；
     * 提现在成功和失败时都会通知商户；其他订单只在成功时会通知商户。
     *
     * 必返
     */
    private String status;

}
