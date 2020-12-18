package com.kt.component.validator.constraints;


import com.kt.component.validator.MoneyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
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