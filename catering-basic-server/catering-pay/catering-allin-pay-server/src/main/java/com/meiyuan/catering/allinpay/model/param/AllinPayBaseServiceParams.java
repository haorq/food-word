package com.meiyuan.catering.allinpay.model.param;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.allinpay.core.bean.BizParameter;
import lombok.Data;

@Data
public class AllinPayBaseServiceParams {
    /**
     * 支持个人会员、企业会员、平台。 若平台，上送固定值： #yunBizUserId_B2C#
     */
    public String bizUserId;

    public AllinPayBaseServiceParams() {
    }

    public AllinPayBaseServiceParams(String bizUserId) {
        this.bizUserId = bizUserId;
    }


    public BizParameter toBizParameter() {
        String string = JSON.toJSONString(this);
        return JSON.parseObject(string, BizParameter.class);
    }
}
