package com.github.julyss2019.mcsp.julylibrary.command.tab;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TabCompleter {
    private JulyTabCommand command;
    private Map<String, String[]> tabMap;

    public TabCompleter(JulyTabCommand command, Map<String, String[]> tabMap) {
        this.command = command;
        this.tabMap = tabMap;
    }

    public Map<String, String[]> getTabMap() {
        return (Map<String, String[]>) ((HashMap) tabMap).clone();
    }

    public JulyTabCommand getCommand() {
        return command;
    }

    public static class Builder {
        private JulyTabCommand command;
        private Map<String, String[]> tabMap = new HashMap<>();

        public Builder command(@NotNull JulyTabCommand command) {
            this.command = command;
            return this;
        }

        public Builder set(String path, String... values) {
            if (tabMap.containsKey(path)) {
                throw new RuntimeException("path 已存在");
            }

            tabMap.put(command.getFirstArg() + "." + path.toLowerCase(), values);
            return this;
        }

        public TabCompleter build() {
            if (command == null) {
                throw new RuntimeException("command 未设置.");
            }

            return new TabCompleter(command, tabMap);
        }
    }
}
