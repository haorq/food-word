package com.meiyuan.catering.admin.annotation;

import java.lang.annotation.*;

/**
 * @Author MeiTao
 * @Date 2020/6/23 0023 17:45
 * @Description 简单描述 : 防重复提交注解
 * @Since version-1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatSubmit {

    long lockTime() default 1L;

}
