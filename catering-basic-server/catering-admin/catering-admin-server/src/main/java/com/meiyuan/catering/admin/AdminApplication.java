package com.meiyuan.catering.admin;

import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yaoozu
 * @description
 * @date 2020/3/1721:36
 * @since v1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class AdminApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AdminApplication.class, args);
    }
}
