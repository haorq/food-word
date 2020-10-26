package com.meiyuan.catering.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = { CantContainsEmojiValidator.class })
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface CantContainsEmoji {

    String message() default "输入项不能包含表情";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
