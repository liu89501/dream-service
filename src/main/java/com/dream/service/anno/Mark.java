package com.dream.service.anno;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mark
{
    int value();

    boolean authenticate() default true;
}
