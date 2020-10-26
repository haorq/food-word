package com.meiyuan.catering.allinpay.model.bean.paymethod;

import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/18 9:39
 * 实名付（单笔）
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class AllinPayRealNamePay {
    /** 银行卡号，RSA 加密。（只支持四 要素及实名付绑定银行卡） */
    private String  bankCardNo;
    private Integer amount;

}
