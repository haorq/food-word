package com.meiyuan.catering.allinpay.model.bean.paymethod;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.order.PayMethodKeyEnums;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/29 11:09
 * @description 通联通代付
 **/

@Data
@Builder
public class WithdrawPay {

    private String payTypeName;
    private String unoinBank;
    private String bankName;
    private String province;
    private String city;

    public JSONObject toJsonObject() {
        this.payTypeName = "withdraw_tlt";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(PayMethodKeyEnums.WITHDRAW_TLT.getCode(), this);
        return jsonObject;
    }

}
