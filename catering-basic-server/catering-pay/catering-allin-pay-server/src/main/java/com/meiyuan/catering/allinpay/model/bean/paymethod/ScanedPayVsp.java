package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/18 10:04
 * 收银宝刷卡支付（被扫）— —支持微信、 支付宝、银联、 手机 QQ
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class ScanedPayVsp {
    private String limitPay;
    private Integer amount;
    /** 支付授权码，支付宝被扫刷卡支付 时,用户的付款二维码 */
    private String authcode;


    public JSONObject toJsonObject() {
        if (this.limitPay == null) {
            this.limitPay = "";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PayMethodKeyEnums.CODEPAY_VSP.getCode(), this);
        return jsonObject;
    }
}
