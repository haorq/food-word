package com.meiyuan.catering.merchant.goods;

import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 方法描述 : MerchantApplication
 * @Author: MeiTao
 * @Date: 2020/6/23 0023 9:26
 * @param
 * @return:
 * @Since version-1.1.0
 */
@SpringBootApplication(scanBasePackages = {"com.meiyuan"})
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class MerchantGoodsApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MerchantGoodsApplication.class, args);
    }
}
