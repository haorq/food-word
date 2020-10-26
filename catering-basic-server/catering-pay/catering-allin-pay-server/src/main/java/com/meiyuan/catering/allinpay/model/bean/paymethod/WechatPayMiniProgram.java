package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import lombok.Builder;
import lombok.Data;

/**
 * created on 2020/8/17 17:24
 * 微信小程序支付（收银宝）
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
@Builder
public class WechatPayMiniProgram {

    /**
     * 非贷记卡：no_credit，
     * 借、贷记卡：""需要传空字符串，不能不传
     */
    private String limitPay;
    /**
     * 微信小程序支付 appid 参数 当商户有多个小程序或公众号时接 口指定上送
     */
    private String subAppid;
    /**
     * 支付金额，单位：分
     */
    private Integer amount;
    /**
     * 微信 JS 支付 openid——微信分配
     */
    private String acct;


    public JSONObject toJsonObject() {
        if (this.limitPay == null) {
            this.limitPay = "";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PayMethodKeyEnums.WECHATPAY_MINIPROGRAM.getCode(), this);
        return jsonObject;
    }

}
