package com.meiyuan.catering.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * @author herui
 */
public class NotSpaceValidator implements ConstraintValidator<NotSpace, String> {

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return !s.contains(" ");
	}
}
