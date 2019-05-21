package com.github.julyss2019.mcsp.julylibrary.test;

import com.github.julyss2019.mcsp.julylibrary.command.tab.TabCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class TestCommand implements TabCommand, CommandExecutor {
    @Override
    public Map<String, String[]> getTabCompleterMap() {
        Map<String, String[]> map = new HashMap<>();

        map.put("test", new String[] {"1", "3", "4"});
        map.put("t2", new String[] {"21", "23", "42"});
        map.put("test.1", new String[] {"4", "6"});
        return map;
    }

    @Override
    public String getPermission() {
        return null;
    }
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("test");
        return false;
    }


}
