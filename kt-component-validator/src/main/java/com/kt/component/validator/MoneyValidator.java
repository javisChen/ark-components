package com.kt.component.validator;

import cn.hutool.core.lang.Validator;
import com.kt.component.validator.constraints.Money;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 金额校验器
 */
public class MoneyValidator implements ConstraintValidator<Money, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Validator.isMoney(value);
    }
}