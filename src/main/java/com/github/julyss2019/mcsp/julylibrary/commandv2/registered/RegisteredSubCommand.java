package com.github.julyss2019.mcsp.julylibrary.commandv2.registered;

import com.github.julyss2019.mcsp.julylibrary.commandv2.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RegisteredSubCommand {
    private RegisteredCommand registeredCommand;
    private SubCommand subCommand;
    private Object obj;
    private Method method;

    public RegisteredSubCommand(@NotNull RegisteredCommand registeredCommand, @NotNull SubCommand subCommand, @NotNull Object obj, @NotNull Method method) {
        this.registeredCommand = registeredCommand;
        this.obj = obj;
        this.subCommand = subCommand;
        this.method = method;
    }

    public RegisteredCommand getRegisteredCommand() {
        return registeredCommand;
    }

    public SubCommand getSubCommand() {
        return subCommand;
    }

    public void execute(@NotNull CommandSender cs, @NotNull String[] args) {
        try {
            method.invoke(obj, cs, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
