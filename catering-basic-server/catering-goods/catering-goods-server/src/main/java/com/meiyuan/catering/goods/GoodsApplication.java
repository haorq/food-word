package com.meiyuan.catering.goods;

import com.meiyuan.catering.goods.service.CateringCategoryService;
import com.meiyuan.marsh.jetcache.anno.config.EnableAdvancedCreateCacheAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableTransactionManagement
@EnableAdvancedCreateCacheAnnotation
public class GoodsApplication {
    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(GoodsApplication.class, args);
        CateringCategoryService cateringCategoryService  = context.getBean(CateringCategoryService.class);
        System.out.println(cateringCategoryService == null);
        System.out.println(cateringCategoryService.del("10000"));
    }

}
