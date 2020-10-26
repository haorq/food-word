package com.meiyuan.catering.allinpay.model.bean.paymethod;

import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/17 17:24
 * 银联扫码支付(正扫) ——收 银宝
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class ScanUnionPay {
    private String limitPay;
    private Integer amount;


}
