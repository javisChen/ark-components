package com.ark.component.validator.constraints;

import com.ark.component.validator.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号码校验注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {

    String message() default "手机号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}