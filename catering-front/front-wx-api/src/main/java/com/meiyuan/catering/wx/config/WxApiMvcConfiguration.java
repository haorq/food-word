package com.meiyuan.catering.wx.config;

import com.meiyuan.catering.wx.annotation.support.LoginUserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author admin
 */
@Configuration
public class WxApiMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
    }

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(tokenInterceptor).excludePathPatterns(excludedPaths);
//	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**")
//				.addResourceLocations("classpath:/static/");
//		registry.addResourceHandler("/webjars/**")
//				.addResourceLocations("classpath:/META-INF/resources/webjars/");
//		registry.addResourceHandler("doc.html", "swagger-ui.html")
//				.addResourceLocations("classpath:/META-INF/resources/");
//	}

}
