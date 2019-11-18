package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.command.JulyCommand;

import java.util.HashMap;
import java.util.Map;

public interface JulyTabCommand extends JulyCommand {
    Map<String, String[]> getTabCompleterMap();
}
