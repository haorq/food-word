package com.meiyuan.catering.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = { ChineseCharactersValidator.class })
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface ChineseCharacters {

    String message() default "输入项只能由汉字组成";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
