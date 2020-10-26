package com.meiyuan.catering.core.util.dada;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh
 */
@Configuration
@EnableConfigurationProperties(DaDaProperties.class)
public class DaDaAutoConfiguration {

    private final DaDaProperties daDaProperties;

    public DaDaAutoConfiguration(DaDaProperties daDaProperties) {
        this.daDaProperties = daDaProperties;
    }

    @Bean
    public DaDaService daDaService() {
        DaDaService daDaService = new DaDaService();
        daDaService.setAppId(daDaProperties.getAppId());
        daDaService.setAppSecret(daDaProperties.getAppSecret());
        daDaService.setIsOnline(daDaProperties.getIsOnline());
        daDaService.setDomain(daDaProperties.getDomain());
        daDaService.setSourceId(daDaProperties.getSourceId());
        return daDaService;
    }
}
