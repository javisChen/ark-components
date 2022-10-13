package com.ark.component.openapi.http;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryParam {

    String name() default "";
}
