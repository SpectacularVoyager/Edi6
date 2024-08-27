package com.ankush.EDI.ReflectionUtils.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLSelect {
    public String stmt() default "";

    public String col() default "";

}
