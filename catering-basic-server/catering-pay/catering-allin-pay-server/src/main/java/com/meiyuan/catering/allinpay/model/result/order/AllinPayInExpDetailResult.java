package com.meiyuan.catering.allinpay.model.result.order;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 18:09
 * @description 通联订单收支明细返回值
 **/

@Data
public class AllinPayInExpDetailResult {

    /**
     * 收支明细流水号
     *
     * 必返
     */
    private String tradeNo;
    /**
     * 账户集名称
     *
     * 必返
     */
    private String accountSetName;
    /**
     * 变更时间
     *
     * yyyy-MM-dd HH:mm:ss
     *
     * 必返
     */
    private String changeTime;
    /**
     * 现有金额
     *
     * 必返
     */
    private Long curAmount;
    /**
     * 原始金额
     *
     * 必返
     */
    private Long oriAmount;
    /**
     * 变更金额
     *
     * 必返
     */
    private Long chgAmount;
    /**
     * 现有冻结金额
     *
     * 必返
     */
    private Long curFreezenAmount;
    /**
     * 商户订单号（支付订单）
     *
     * 非必返
     */
    private String bizOrderNo;
    /**
     * 备注
     *
     * 非必返
     */
    private String extendInfo;

}
