package com.github.julyss2019.mcsp.julylibrary.commandv2;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    String firstArg();

    /**
     * 子命令长度
     * @return -1 代表接受所有长度的子命令
     */
    int length();
    String[] subArgs() default {};
    String description();
    String permission() default "";
    SenderType[] senders() default {SenderType.CONSOLE, SenderType.PLAYER};
}
