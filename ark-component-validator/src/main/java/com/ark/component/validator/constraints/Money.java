package com.ark.component.validator.constraints;


import com.ark.component.validator.MoneyValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 金额校验注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MoneyValidator.class)
public @interface Money {

    String message() default "金额格式不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}