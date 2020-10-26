package com.meiyuan.catering.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 短信限制
 * @author admin
 */
@Configuration
@ConfigurationProperties(prefix = "catering.sms")
@Data
public class SmsProperties {

    /**
     * 是否发送验证码
     */
    private Boolean code;
    /**
     * 默认验证码
     */
    private String defaultCode;

    /**
     * 支付成功是否发送取餐码
     */
    private Boolean pay;

}
