package com.github.julyss2019.mcsp.julylibrary.commandv2.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class JulyTabHandler implements TabCompleter {
    private Map<String, Tab> tabMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER); // 忽略大小写

    public void addTab(@NotNull Tab tab) {
        if (hasTab(tab)) {
            throw new RuntimeException("已经存在相同 Arg 的 Tab 了");
        }

        tabMap.put(tab.getArg(), tab);
    }

    public boolean hasTab(@NotNull Tab tab) {
        return hasTab(tab.getArg());
    }

    public boolean hasTab(@NotNull String arg) {
        return tabMap.containsKey(arg);
    }

    public void removeTab(@NotNull Tab tab) {
        tabMap.remove(tab.getArg());
    }

    public Tab getTab(@NotNull String arg) {
        return tabMap.get(arg);
    }

    public Collection<Tab> getTabs() {
        return new ArrayList<>(tabMap.values());
    }

    /**
     * 得到补全文本列表
     * @param args 必须
     * @return
     */
    protected List<String> completeArgs(@NotNull CommandSender commandSender, @NotNull String[] args) {
        String endArg = args[args.length - 1]; // 最后一个 arg

        if (args.length == 1) {
            return new ArrayList<>(tabMap.values()).stream()
                    .filter(tab -> {
                        if (tab.hasShowPredicate() && !tab.getShowPredicate().test(commandSender)) {
                            return false;
                        }

                        String arg = tab.getArg();

                        return arg.toLowerCase().startsWith(endArg.toLowerCase());
                    })
                    .map(Tab::getArg)
                    .collect(Collectors.toList()); // 根据最后一个arg筛选
        }

        Tab finalTab = tabMap.get(args[0]);

        if (finalTab == null) {
            return null;
        }


        // 递归得到最终 Tab
        for (int i = 1; i < args.length - 1; i++) {
            String arg = args[i];

            if (finalTab.hasSubTab(arg)) {
                finalTab = finalTab.getSubTab(arg);
            } else {
                return null;
            }
        }

        if (finalTab == null) {
            return null;
        }

        List<String> result = new ArrayList<>();

        for (Tab tab : finalTab.getSubTabs()) {
            if (!tab.hasShowPredicate() || (tab.hasShowPredicate() && tab.getShowPredicate().test(commandSender))) {
                result.add(tab.getArg());
            }
        }

        return result.size() == 0 ? null : result.stream().filter(s -> s.toLowerCase().startsWith(endArg.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return completeArgs(commandSender, args);
    }
}
