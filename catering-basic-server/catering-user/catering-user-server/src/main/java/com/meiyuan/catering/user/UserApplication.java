package com.meiyuan.catering.user;

import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author admin
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class UserApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UserApplication.class, args);
    }
}
