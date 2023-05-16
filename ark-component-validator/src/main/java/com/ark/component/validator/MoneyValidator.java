package com.ark.component.validator;

import cn.hutool.core.lang.Validator;
import com.ark.component.validator.constraints.Money;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 金额校验器
 */
public class MoneyValidator implements ConstraintValidator<Money, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Validator.isMoney(value);
    }
}