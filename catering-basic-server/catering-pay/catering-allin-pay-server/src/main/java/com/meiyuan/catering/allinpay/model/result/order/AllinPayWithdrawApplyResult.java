package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 17:09
 * @description 通联提现申请返回值
 **/

@Data
public class AllinPayWithdrawApplyResult {

    /**
     * 支付状态
     *
     * 仅交易验证方式为“0”时返回
     * 成功：success
     * 进行中：pending
     * 失败：fail
     * 订单成功时会发订单结果通知商户。
     *
     * 非必返
     */
    private String payStatus;
    /**
     * 支付失败信息
     *
     * 仅交易验证方式为“0”时返回
     * 只有payStatus为fail时有效
     *
     * 非必返
     */
    private String payFailMessage;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 仅交易验证方式为“0”时返回
     * 平台，返回: #yunBizUserId_B2C#
     *
     * 非必返
     */
    private String bizUserId;
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
     * 扩展信息
     *
     * 商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     *
     * 非必返
     */
    private String extendInfo;

}
