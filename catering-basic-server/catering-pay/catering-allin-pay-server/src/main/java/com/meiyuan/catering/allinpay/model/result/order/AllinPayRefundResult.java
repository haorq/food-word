package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/25 16:09
 * @description 通联退款申请返回值
 **/

@Data
public class AllinPayRefundResult {

    /**
     * 	支付状态
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
     * 本次退款总金额	单位：分
     *
     * 必返
     */
    private Long amount;
    /**
     * 代金券退款金额	单位：分
     *
     * 非必返
     */
    private Long couponAmount;
    /**
     * 手续费退款金额	单位：分
     *
     * 非必返
     */
    private Long feeAmount;
    /**
     * 扩展信息
     *
     * 接口将原样返回，最多50个字符，不可包含“|”特殊字符
     *
     * 非必返
     */
    private String extendInfo;


}
