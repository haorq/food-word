package com.meiyuan.catering.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = { NotSpaceValidator.class })
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface NotSpace {

    String message() default "输入项不能包含空格";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
