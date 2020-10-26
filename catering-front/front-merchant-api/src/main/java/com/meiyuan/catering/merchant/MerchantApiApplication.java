package com.meiyuan.catering.merchant;

import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yaoozu
 * @description 商户API
 * @date 2020/3/1817:43
 * @since v1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
@EnableSwagger2Doc
public class MerchantApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MerchantApiApplication.class, args);
    }
}
