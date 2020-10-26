package com.meiyuan.catering.core.util.dada.config;

import com.meiyuan.catering.core.util.dada.DaDaService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * DATE: 18/9/3
 *
 * @author: wan
 */
@Getter
public class AppConfig {

    private String appKey;

    private String appSecret;

    private String host;

    private String sourceId;

    private Boolean isOnline;

    public AppConfig(boolean isOnline, String appKey, String appSecret, String sourceId) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.sourceId = isOnline ? sourceId : AppConstant.QA_SOURCE_ID;
        this.isOnline = isOnline;
        this.host = isOnline ? AppConstant.ONLINE_HOST : AppConstant.QA_HOST;
    }

}
