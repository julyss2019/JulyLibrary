package com.github.julyss2019.mcsp.julylibrary.commandv2.registered;

import com.github.julyss2019.mcsp.julylibrary.commandv2.MainCommand;
import com.github.julyss2019.mcsp.julylibrary.validate.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegisteredCommand {
    private MainCommand mainCommand;
    private Set<RegisteredSubCommand> registeredSubCommands;

    public RegisteredCommand() {}

    public void setMainCommand(MainCommand mainCommand) {
        if (this.mainCommand != null) {
            throw new UnsupportedOperationException();
        }

        this.mainCommand = mainCommand;
    }

    public void setRegisteredSubCommands(@NotNull Set<RegisteredSubCommand> registeredSubCommands) {
        if (this.registeredSubCommands != null) {
            throw new UnsupportedOperationException();
        }

        registeredSubCommands.forEach(registeredSubCommand -> {
            if (registeredSubCommand == null) {
                throw new RuntimeException("RegisteredSubCommand 不能为 null");
            }
        });

        this.registeredSubCommands = new HashSet<>(registeredSubCommands);
    }

    public MainCommand getMainCommand() {
        return mainCommand;
    }

    public Collection<RegisteredSubCommand> getRegisteredSubCommands() {
        return new HashSet<>(registeredSubCommands);
    }
}
