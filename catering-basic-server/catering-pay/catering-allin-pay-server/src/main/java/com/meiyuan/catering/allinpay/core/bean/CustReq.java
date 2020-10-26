package com.meiyuan.catering.allinpay.core.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/9/26 14:03
 * @since v1.1.0
 */
@Data
public class CustReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "PAYEE_ACCT_NO")
    private String payeeAcctNo;

    @JSONField(name = "PAYEE_ACCT_NAME")
    private String payeeAcctName;

    @JSONField(name = "AMOUNT")
    private String amount;

    @JSONField(name = "SUMMARY")
    private String summary;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.SortField);
    }
}
