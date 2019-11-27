package com.github.julyss2019.mcsp.julylibrary.command.tab;

import java.util.HashMap;
import java.util.Map;

public class TabCompleter {
    private JulyTabCommand command;
    private Map<String, String[]> tabMap = new HashMap<>();

    public TabCompleter(JulyTabCommand command) {
        this.command = command;
    }

    public TabCompleter set(String path, String... values) {
        if (tabMap.containsKey(path)) {
            throw new RuntimeException("path 已存在");
        }

        tabMap.put(path.toLowerCase(), values);
        return this;
    }

    public JulyTabCommand getCommand() {
        return command;
    }

    public Map<String, String[]> getTabMap() {
        return (Map<String, String[]>) ((HashMap) tabMap).clone();
    }
}
