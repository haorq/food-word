package com.meiyuan.catering.core.check;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author MeiTao
 * @Description ali敏感词过滤配置
 * @Date  2020/1/13 0013 15:45
 */
@Component
@Data
public class BaiDuSenWordFilterProperties {
    @Value("${baidu.appId:}")
    private String appId;
    @Value("${baidu.apiKey:}")
    private String apiKey;
    @Value("${baidu.secretKey:}")
    private String secretKey;
    @Value("${baidu.connectionTimeout:10}")
    private Integer connectionTimeout;
    @Value("${baidu.socketTimeout:10}")
    private Integer socketTimeout;
}
