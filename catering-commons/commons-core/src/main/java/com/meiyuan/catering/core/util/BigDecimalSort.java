package com.meiyuan.catering.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zengzhangni
 * @date 2020/9/18 10:55
 * @since v1.1.0
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface BigDecimalSort {

    boolean serialization() default false;

    int scale() default 2;

    int roundingMode() default BigDecimal.ROUND_HALF_UP;
}
