package com.ark.component.validator;

import cn.hutool.core.lang.Validator;
import com.ark.component.validator.constraints.Mobile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 手机号码校验器
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Validator.isMobile(value);
    }
}