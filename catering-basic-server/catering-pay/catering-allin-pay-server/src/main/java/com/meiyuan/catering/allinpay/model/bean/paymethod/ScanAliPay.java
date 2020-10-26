package com.meiyuan.catering.allinpay.model.bean.paymethod;

import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/17 17:24
 * 支付宝扫码支付(正扫) —— 收银宝
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class ScanAliPay {
    private String limitPay;
    private Integer amount;



}
