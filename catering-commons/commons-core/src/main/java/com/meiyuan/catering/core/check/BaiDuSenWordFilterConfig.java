package com.meiyuan.catering.core.check;

import com.baidu.aip.contentcensor.AipContentCensor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author MeiTao
 * @Description 敏感词过滤
 * @Date 2020/2/11 0012 9:42
 */
@Configuration
public class BaiDuSenWordFilterConfig {
    @Resource
    private BaiDuSenWordFilterProperties constant;

    @Bean
    public AipContentCensor aipContentCensor(){
        AipContentCensor contentCensorClient = new AipContentCensor(
                constant.getAppId(),
                constant.getApiKey(),
                constant.getSecretKey());
        contentCensorClient.setConnectionTimeoutInMillis(constant.getConnectionTimeout());
        contentCensorClient.setSocketTimeoutInMillis(constant.getSocketTimeout());
        return contentCensorClient;
    }
}
