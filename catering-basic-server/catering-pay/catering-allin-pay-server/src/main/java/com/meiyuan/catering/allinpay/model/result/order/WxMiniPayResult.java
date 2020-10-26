package com.meiyuan.catering.allinpay.model.result.order;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/10/19 10:11
 * @since v1.5.0
 */
@Data
public class WxMiniPayResult implements Serializable {

    private String appId;
    private String timeStamp;
    private String nonceStr;
    @JSONField(name = "package")
    private String packageValue;
    private String signType;
    private String paySign;
}
