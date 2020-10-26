package com.meiyuan.catering.allinpay.model.bean.paymethod;

import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/17 17:24
 * 收银宝支付宝 APP 支付
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class AliPayAppVsp {
    private String limitPay;
    private Integer amount;



}
