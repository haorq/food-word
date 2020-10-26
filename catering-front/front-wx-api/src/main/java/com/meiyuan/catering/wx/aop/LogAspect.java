package com.meiyuan.catering.wx.aop;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lh
 * @version 1.2.0
 * @date 2020-07-08
 * 切面定义，用于api调用请求地址和请求参数以及接口响应时间打印
 */

@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final long MILLISECOND = 1000;
    private static final long MINUTE = 60;
    private static final String NULL = "null";
    private static final String EMPTY = "{}";

    @Pointcut("execution(* com.meiyuan.catering.wx.api..*.*(..))")
    public void log() {

    }


    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        long endTime;
        String clazzName;
        String methodName;
        String params = null;
        // 定义返回对象、得到方法需要的参数
        Object resultData = null;
        Object[] args = joinPoint.getArgs();
        clazzName = joinPoint.getTarget().getClass().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        methodName = signature.getName();
        if (args != null && args.length > 0) {
            // - 有参数
            String[] parameterNames = signature.getParameterNames();
            Object formatParams = getParams(parameterNames, args);
            params = formatJson(JSON.toJSON(formatParams).toString());

        }
        log.debug(clazzName + "." + methodName + "：" + params);
        resultData = joinPoint.proceed(args);
        endTime = System.currentTimeMillis();
        log.debug(formatRequestTime(startTime, endTime));
        return resultData;
    }

    private String formatRequestTime(long startTime, long endTime) {
        StringBuffer sb = new StringBuffer();
        sb.append("用时：");
        long time = endTime - startTime;
        if (time < MILLISECOND) {
            sb.append(time);
            sb.append("ms");
            // - 小于1秒，不展示接口响应时长
//            sb.setLength(0);
        } else {
            // - 转换为秒
            long seconds = time / MILLISECOND;
            if (seconds < MINUTE) {
                // - 小于1分钟
                sb.append(seconds);
                sb.append("s");
                long mSeconds = time % MILLISECOND;
                if (mSeconds > 0L) {
                    // - 毫秒
                    sb.append(mSeconds);
                    sb.append("ms");
                }
            } else {
                // - 超过1分钟，转换为分钟
                long m = seconds / MINUTE;
                long s = seconds % MINUTE;
                sb.append(m);
                sb.append("m");
                sb.append(s);
                sb.append("s");
                long mSeconds = time % MILLISECOND;
                if (mSeconds > 0L) {
                    // - 毫秒
                    sb.append(mSeconds);
                    sb.append("ms");
                }
            }
        }
        return sb.toString();
    }


    private Object getParams(String[] paramNames, Object[] paramValues) {
        if (ArrayUtils.isEmpty(paramNames) || ArrayUtils.isEmpty(paramValues)) {
            return null;
        }
        Map<String, Object> paramMap = new HashMap<String, Object>(16);
        for (int i = 0; i < paramNames.length; i++) {
            //参数名
            String name = paramNames[i];
            //参数值
            Object value = paramValues[i];
            if (value == null) {
                paramMap.put(name, null);
                continue;
            }
            paramMap.put(name, value);
        }
        return paramMap;
    }

    private String formatJson(String jsonStr) {

        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        if (NULL.equals(jsonStr)) {
            return null;
        }
        if (EMPTY.equals(jsonStr)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

}
