package com.github.julyss2019.mcsp.julylibrary.commandv2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MainCommand {
    String firstArg();
    String description();
    String permission() default "";
    int priority() default 0;
}
