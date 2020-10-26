package com.meiyuan.catering.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lhm
 * @date 2020/4/1 14:16
 **/
@Configuration
@ConfigurationProperties(prefix = "map.baidu")
@Data
public class BaiDuApiProperties {



    private String ak;
}
