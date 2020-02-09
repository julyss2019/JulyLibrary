package com.github.julyss2019.mcsp.julylibrary.commandv2.registered;

import com.github.julyss2019.mcsp.julylibrary.commandv2.JulyCommand;
import com.github.julyss2019.mcsp.julylibrary.utils.ValidateUtil;
import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegisteredCommand {
    private JulyCommand julyCommand;
    private Set<RegisteredSubCommand> registeredSubCommands = new HashSet<>();

    public RegisteredCommand(@NotNull JulyCommand julyCommand, @NotNull Collection<RegisteredSubCommand> registeredSubCommands) {
        this.julyCommand = julyCommand;

        registeredSubCommands.forEach(registeredSubCommand -> this.registeredSubCommands.add(ValidateUtil.notNull(registeredSubCommand, new RuntimeException("元素不能为null"))));
    }

    public JulyCommand getJulyCommand() {
        return julyCommand;
    }

    public Collection<RegisteredSubCommand> getRegisteredSubCommands() {
        return new HashSet<>(registeredSubCommands);
    }
}
