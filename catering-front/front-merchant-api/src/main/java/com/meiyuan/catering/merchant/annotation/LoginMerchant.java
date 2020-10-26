package com.meiyuan.catering.merchant.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yaoozu
 * @description 登录商户注解
 * @date 2020/3/2013:45
 * @since v1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginMerchant {
    boolean required() default true;
}
