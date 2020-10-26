package com.meiyuan.catering.admin.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * @author admin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String value() default "";
}
