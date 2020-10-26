package com.meiyuan.catering.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author herui
 */
public class ChineseCharactersValidator implements ConstraintValidator<ChineseCharacters, String> {

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		int n = 0;
		for(int i = 0; i < s.length(); i++) {
			n = (int)s.charAt(i);
			if(!(19968 <= n && n <40869)) {
				return false;
			}
		}
		return true;
	}
}
