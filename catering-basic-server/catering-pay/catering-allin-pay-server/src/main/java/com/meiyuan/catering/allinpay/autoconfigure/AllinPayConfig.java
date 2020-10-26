package com.meiyuan.catering.allinpay.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "catering.allinpay")
@Data
public class AllinPayConfig {
    private String url;
    private String appId;
    private String secretKey;
    private String certPath;
    private String certPwd;
    private String tlCertPath;
    private String notifyUrl;
    private String format = "JSON";
    private String charset = "utf-8";
    private String signType = "SHA256WithRSA";
    private String version = "1.0";
    private String domain;
    private String domainH5;
    /**
     * 托管账户集编号
     */
    private String accountSetNo;
    /**
     * 收银宝集团商户号
     */
    private String vspMerchantId;
    /**
     * 集团模式：上送集团商户号 appid
     */
    private String vspMerchantAppId;
    /**
     * 收银宝商户号 集团模式：收银宝子商户号
     */
    private String vspCusId;
    /**
     * 微信小程序 appId
     */
    private String wxAppId;
    /**
     * 补贴账户编号
     */
    private String subsidyAccountNo;
    /**
     * 内扣账户编号
     */
    private String splitAccountNo;

}
