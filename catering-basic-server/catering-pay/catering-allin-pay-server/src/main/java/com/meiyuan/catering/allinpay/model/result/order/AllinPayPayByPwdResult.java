package com.meiyuan.catering.allinpay.model.result.order;

import com.meiyuan.catering.allinpay.model.result.AllinPayBaseResponseResult;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 09:09
 * @description 通联确认支付（前台+密码验证版）返回值
 **/

@Data
public class AllinPayPayByPwdResult extends AllinPayBaseResponseResult {

    /**
     * 支付状态
     *
     * 仅交易验证方式为“0”时返回
     * 成功：success
     * 进行中：pending
     * 失败：fail
     * 订单成功时会发订单结果通知商户。
     *
     * 必返
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
     * 商户订单号（支付订单）
     *
     * 必返
     */
    private String bizOrderNo;

}
