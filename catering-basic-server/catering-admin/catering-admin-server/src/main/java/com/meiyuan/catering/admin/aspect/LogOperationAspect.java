package com.meiyuan.catering.admin.aspect;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.admin.annotation.LogOperation;
import com.meiyuan.catering.admin.entity.CateringAdmin;
import com.meiyuan.catering.admin.log.SysLogOperation;
import com.meiyuan.catering.admin.log.enums.LogTypeEnum;
import com.meiyuan.catering.admin.log.producer.LogProducer;
import com.meiyuan.catering.core.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作日志，切面处理类
 *
 * @author admin
 */
@Aspect
@Component
@Slf4j
public class LogOperationAspect {
    @Autowired
    private LogProducer logProducer;

    @Pointcut("@annotation(com.meiyuan.catering.admin.annotation.LogOperation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        try {
            //执行方法
            Object result = point.proceed();

            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, 1);

            return result;
        } catch (Exception e) {
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            //保存日志
            saveLog(point, time, 0);

            throw e;
        }
    }


    private void saveLog(ProceedingJoinPoint joinPoint, long time, Integer status) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLogOperation logOperation = new SysLogOperation();
        LogOperation annotation = method.getAnnotation(LogOperation.class);
        if (annotation != null) {
            //注解上的描述
            logOperation.setOperation(annotation.value());
        }
        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        Object info = request.getAttribute("info");
        if (Objects.nonNull(info)) {
            CateringAdmin admin = (CateringAdmin) info;
            //登录用户信息
            logOperation.setCreateBy(admin.getId().toString());
            logOperation.setCreatorName(admin.getUsername());
        }


        logOperation.setType(LogTypeEnum.OPERATION.value());
        logOperation.setStatus(status);
        logOperation.setRequestTime((int) time);
        logOperation.setCreateTime(LocalDateTime.now());


        logOperation.setIp(HttpContextUtils.getRealIp(request));
        logOperation.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        logOperation.setRequestUri(request.getRequestURI());
        logOperation.setRequestMethod(request.getMethod());

        //请求参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args[0]);
            logOperation.setRequestParams(params);
        } catch (Exception ignored) {
        }
        log.debug("\n日志信息:{}", logOperation);

        //保存到Redis队列里
        logProducer.saveLog(logOperation);
    }
}
