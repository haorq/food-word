package com.meiyuan.catering.allinpay.model.bean.pay;

import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/25 09:09
 * @description 通联微信APP支付返回信息
 **/

@Data
public class AllinPayWeChatAppInfoResult {

    /**
     * 签名
     */
    private String sign;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 随机字符串
     */
    private String noncestr;
    /**
     * 商户号
     */
    private String partnered;
    /**
     * 预支付交易会话
     */
    private String prepayid;
    /**
     * 扩展字段
     *
     * 通联原生返回字段为"package"
     */
    private String packageStr;
    /**
     * 应用ID
     */
    private String appid;

}
