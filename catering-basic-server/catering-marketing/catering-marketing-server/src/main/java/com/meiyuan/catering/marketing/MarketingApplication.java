package com.meiyuan.catering.marketing;

import com.meiyuan.catering.marketing.dao.CateringMarketingSeckillMapper;
import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MarketingApplication
 * @Description
 * @Author gz
 * @Date 2020/3/16 15:51
 * @Version 1.1
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class MarketingApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MarketingApplication.class, args);

        System.out.println(context.getBean(CateringMarketingSeckillMapper.class) == null);
    }
}
