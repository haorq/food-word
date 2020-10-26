package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 10:09
 * @description 通联支付单笔托管代付（标准版）返回值
 **/

@Data
public class AllinPaySignalAgentPayResult {

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
     * 通商云订单号
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 代付去向
     *
     * 1：到账户余额
     * 2：到银行卡
     */
    private Long payWhereabouts;
    /**
     * 扩展信息
     * 商户拓展参数，用于透传给商户，不可包含“\
     *
     * 非必返
     */
    private String extendInfo;

}
