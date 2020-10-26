package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/17 17:24
 * 微信扫码支付(正扫)——收银宝
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class ScanWechatPay {
    private String limitPay;
    private Integer amount;


    public JSONObject toJsonObject() {
        if (this.limitPay == null) {
            this.limitPay = "";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PayMethodKeyEnums.SCAN_WEIXIN.getCode(), this);
        return jsonObject;
    }

}
