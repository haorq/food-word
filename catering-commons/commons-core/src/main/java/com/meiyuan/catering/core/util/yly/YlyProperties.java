package com.meiyuan.catering.core.util.yly;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 易联云打印机配置
 * @author lh
 */
@Data
@ConfigurationProperties(prefix = "catering.yly")
public class YlyProperties {
    private String appId;
    private String appSecret;
    private String appToken;
    private String imgUrl;
}
