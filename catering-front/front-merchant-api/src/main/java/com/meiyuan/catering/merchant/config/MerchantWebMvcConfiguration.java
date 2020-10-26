package com.meiyuan.catering.merchant.config;

import com.meiyuan.catering.merchant.annotation.support.LoginMerchantHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 10:29
 * @Description 简单描述 :  MerchantWebMvcConfiguration
 * @Since version-1.0.0
 */
@Configuration
public class MerchantWebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginMerchantHandlerMethodArgumentResolver());
	}
}
