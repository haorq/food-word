package com.meiyuan.catering.wx;

import com.meiyuan.catering.es.annotation.EnableESTools;
import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author admin
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableSwagger2Doc
@EnableAdvancedCreateCacheAnnotation
@EnableESTools(basePackages = "com.meiyuan.catering.es.entity")
public class WechatApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApiApplication.class, args);
    }
}
