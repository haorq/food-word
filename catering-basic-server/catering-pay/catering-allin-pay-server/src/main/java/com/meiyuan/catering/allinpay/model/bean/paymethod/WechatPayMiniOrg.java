package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import lombok.Builder;
import lombok.Data;

/**
 * 描述: 微信小程序支 付_集团（收银 宝）
 *
 * @author zengzhangni
 * @return
 * @date 2020/10/16 10:14
 * @since v1.5.0
 */
@Data
@Builder
public class WechatPayMiniOrg {

    /**
     * 收银宝子商户号
     */
    private String vspCusid;
    /**
     * 微信小程序支付 appid 参数 当商户有多个小程序或公众号时接 口指定上送
     */
    private String subAppid;
    /**
     * 非贷记卡：no_credit，
     * 借、贷记卡：""需要传空字符串，不能不传
     */
    private String limitPay;
    /**
     * 微信 JS 支付 openid——微信分配
     */
    private String acct;
    /**
     * 支付金额，单位：分
     */
    private Integer amount;


    public JSONObject toJsonObject() {
        if (this.limitPay == null) {
            this.limitPay = "";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PayMethodKeyEnums.WECHATPAY_MINIPROGRAM_ORG.getCode(), this);
        return jsonObject;
    }

}
