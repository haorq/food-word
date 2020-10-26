package com.meiyuan.catering.job.config;

import com.meiyuan.catering.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;

/**
 * @ClassName TokenInterceptor
 * @Description
 * @Author gz
 * @Date 2020/4/26 10:09
 * @Version 1.1
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String timestamp = request.getHeader("timestamp");
        // token xxl-job请求头定义的
        String token = request.getHeader("token");
        log.debug("xxl-job请求头信息:timestamp={};token={}",timestamp,token);
        if(StringUtils.isBlank(timestamp)||StringUtils.isBlank(token)){
            throw new CustomException("没有获取到token或timestamp");
        }
        return jobMatcher(token,timestamp);
    }




    private boolean jobMatcher(String token, String timestamp) {
        String nowToken = md5((timestamp + "job"));
        if (StringUtils.isNotEmpty(timestamp)
                && StringUtils.isNotEmpty(token)
                && token.equals(nowToken)) {
            return true;
        }
        return false;
    }

    private String md5(String source) {
        StringBuffer sb = new StringBuffer(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }
}
