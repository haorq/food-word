package com.meiyuan.catering.merchant.pc;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.meiyuan.catering.es.annotation.EnableESTools;
import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MerchantPcApiApplication
 * @Description
 * @Author gz
 * @Date 2020/6/30 9:33
 * @Version 1.2.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.meiyuan")
@EnableSwagger2Doc
@EnableESTools(basePackages = {"com.meiyuan.catering.es.entity"})
public class MerchantPcApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MerchantPcApiApplication.class, args);
    }
}
