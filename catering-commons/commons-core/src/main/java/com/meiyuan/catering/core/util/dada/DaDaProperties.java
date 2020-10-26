package com.meiyuan.catering.core.util.dada;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 达达配置
 *
 * @author lh
 */
@Data
@ConfigurationProperties(prefix = "catering.delivery.dada")
public class DaDaProperties {
    private String appId;
    private String appSecret;
    private Boolean isOnline;
    private String domain;
    private String sourceId;
}
