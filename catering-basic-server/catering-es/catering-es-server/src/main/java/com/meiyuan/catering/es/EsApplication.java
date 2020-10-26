package com.meiyuan.catering.es;

import com.meiyuan.catering.es.annotation.EnableESTools;
import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yaoozu
 * @description
 * @date 2020/3/1519:09
 * @since v1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableESTools(basePackages = {"com.meiyuan.catering.es.entity"})
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

}
