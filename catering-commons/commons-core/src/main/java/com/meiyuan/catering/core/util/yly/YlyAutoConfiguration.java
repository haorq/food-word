package com.meiyuan.catering.core.util.yly;

import com.yly.java.yly_sdk.RequestMethod;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh
 */
@Configuration
@EnableConfigurationProperties(YlyProperties.class)
public class YlyAutoConfiguration {

    private final YlyProperties properties;

    public YlyAutoConfiguration(YlyProperties properties) {
        this.properties = properties;
    }

    @Bean
    public YlyService ylyService() {
        YlyService ylyService = new YlyService();
        ylyService.setAppId(this.properties.getAppId());
        ylyService.setAppSecret(this.properties.getAppSecret());
        ylyService.setAppToken(this.properties.getAppToken());
        ylyService.setImgUrl(this.properties.getImgUrl());
        RequestMethod.getInstance().init(this.properties.getAppId(), this.properties.getAppSecret());
        return ylyService;
    }


}
