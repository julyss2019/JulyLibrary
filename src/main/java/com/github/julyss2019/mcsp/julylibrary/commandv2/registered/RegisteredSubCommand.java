package com.github.julyss2019.mcsp.julylibrary.commandv2.registered;

import com.github.julyss2019.mcsp.julylibrary.commandv2.JulyCommand;
import com.github.julyss2019.mcsp.julylibrary.commandv2.SubCommandHandler;
import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RegisteredSubCommand {
    private JulyCommand julyCommand;
    private SubCommandHandler subCommandHandler;
    private Method method;

    public RegisteredSubCommand(@NotNull JulyCommand julyCommand, @NotNull SubCommandHandler subCommandHandler, @NotNull Method method) {
        this.julyCommand = julyCommand;
        this.subCommandHandler = subCommandHandler;
        this.method = method;
    }

    public JulyCommand getJulyCommand() {
        return julyCommand;
    }

    public SubCommandHandler getSubCommandHandler() {
        return subCommandHandler;
    }

    private Method getMethod() {
        return method;
    }

    public void execute(@NotNull CommandSender cs, @NotNull String[] args) {
        try {
            method.invoke(julyCommand, cs, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
