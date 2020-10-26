package com.meiyuan.catering.wx.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaoozu
 * @description 限流
 * @date 2020/2/2710:32
 * @since v1.0.0
 */
@Component
@Slf4j
public class LimitInterceptor implements HandlerInterceptor {
    private AtomicInteger active = new AtomicInteger();
    @Value("${catering.limit.max:1000}")
    private Long maxActive;
    @Value("${catering.limit.url:}")
    private String limitUrl;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isLimit(request)){
            if (active.get() > maxActive){
                log.debug("limit interceptor active:"+active);
                return false;
            }
            active.incrementAndGet();
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       if (isLimit(request)){
           active.decrementAndGet();
       }
    }

    private boolean isLimit(HttpServletRequest request){
        UrlPathHelper helper = new UrlPathHelper();
        //当前请求路径
        String lookupPath = helper.getLookupPathForRequest(request);
        if (limitUrl != null && lookupPath.indexOf(limitUrl) >= 0){
            return true;
        }
        return false;
    }
}
