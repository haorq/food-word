package com.meiyuan.catering.allinpay.model.bean.pay;

import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/21 11:02
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class UnifiedOrderResult {
    /** 交易编号 */
    private String tradeNo;
    /**
     * POS支付的付款码
     */
    private String payCode;
    /**
     * 扫码支付信息/ JS 支付信息（微信、支付宝、QQ钱包）/ 微信小程序
     */
    private String payInfo;
}
