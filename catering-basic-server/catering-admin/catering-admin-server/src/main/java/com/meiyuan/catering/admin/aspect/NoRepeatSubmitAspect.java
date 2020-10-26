package com.meiyuan.catering.admin.aspect;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.admin.annotation.NoRepeatSubmit;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.HttpContextUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NoRepeatSubmitAspect
 * @Description
 * @Author gz
 * @Date 2020/5/9 9:42
 * @Version 1.1
 */
@Aspect
@Component
public class NoRepeatSubmitAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(NoRepeatSubmitAspect.class);
    @AdvancedCreateCache(@CreateCache(area = JetcacheAreas.ADMIN_AREA,name = JetcacheNames.REPEAT_SUBMIT))
    private AdvancedCache lockCache;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit){}

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint point,NoRepeatSubmit noRepeatSubmit) throws Throwable{
        long lockSeconds = noRepeatSubmit.lockTime();
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Assert.notNull(request,"request can not null");
        String token = request.getHeader("X-Dts-Admin-Token");
        String path = request.getRequestURI();
        String key = getKey(token, path);
        AutoReleaseLock lock = lockCache.tryLock(key, lockSeconds, TimeUnit.SECONDS);
        if(lock!=null){
            Object result;
            try{
               result = point.proceed();
            }finally {
                lock.close();
                LOGGER.info("releaseLock success,key=[{}]",key);
            }
            return result;
        }
        return Result.fail("重复请求!请稍后再试");
    }

    private String getKey(String token,String path){
        return token+"-"+path;
    }

}
