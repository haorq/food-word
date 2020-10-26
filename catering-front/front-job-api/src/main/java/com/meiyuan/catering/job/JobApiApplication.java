package com.meiyuan.catering.job;

import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yaoozu
 * @description 定时任务API
 * @date 2020/3/1817:43
 * @since v1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class JobApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApiApplication.class, args);
    }
}
