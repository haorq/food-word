package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/29 14:09
 * @description 通联确认支付（后台+短信验证码确认）返回值
 **/

@Data
public class AllinPayPayByBackSmsResult {

    /**
     * 支付状态
     *
     * 仅交易验证方式为“0”时返回
     * 成功：success
     * 进行中：pending
     * 失败：fail
     * 订单成功时会发订单结果通知商户。
     */
    private String payStatus;
    /**
     * 支付失败信息
     *
     * 仅交易验证方式为“0”时返回
     * 只有payStatus为fail时有效
     */
    private String payFailMessage;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 仅交易验证方式为“0”时返回
     * 平台，返回:#yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;

}
