package com.meiyuan.catering.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author admin
 */
@Configuration
@ConfigurationProperties(prefix = "catering.wx")
@Data
public class WxProperties {

    private String appPublicId;

    private String appPublicSecret;

    private String appId;

    private String appSecret;

    private String wxToken;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String rechargeNotifyUrl;

    private String keyPath;

}
