package com.github.julyss2019.mcsp.julylibrary.command.tab;

import java.util.HashMap;
import java.util.Map;

public interface JulyTabCommand {
    default Map<String, String[]> getTabCompleterMap() {
        return new HashMap<>();
    }

    default String getPermission() {
        return "";
    }
}
