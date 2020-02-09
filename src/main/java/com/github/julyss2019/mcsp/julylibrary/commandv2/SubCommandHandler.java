package com.github.julyss2019.mcsp.julylibrary.commandv2;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandHandler {
    String firstArg();
    int length();
    String[] subArgs() default {};
    String description();
    String permission() default "";
    SenderType[] senders() default {SenderType.CONSOLE, SenderType.PLAYER};
}
