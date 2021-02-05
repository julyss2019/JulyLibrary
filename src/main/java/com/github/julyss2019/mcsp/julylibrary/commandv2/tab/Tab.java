package com.github.julyss2019.mcsp.julylibrary.commandv2.tab;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class Tab {
    private final String arg;
    private final  @Nullable Predicate<CommandSender> showPredicate; // 显示条件
    private final Map<String, Tab> subTabMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Tab(@NotNull String arg) {
        this(arg, null);
    }

    public Tab(@NotNull String arg, @Nullable Predicate<CommandSender> showPredicate) {
        this.arg = arg;
        this.showPredicate = showPredicate;
    }

    public Tab getSubTab(@NotNull String tag) {
        return subTabMap.get(tag);
    }

    public Collection<Tab> getSubTabs() {
        return new ArrayList<>(subTabMap.values());
    }

    public boolean hasShowPredicate() {
        return showPredicate != null;
    }

    public @Nullable Predicate<CommandSender> getShowPredicate() {
        return showPredicate;
    }

    public String getArg() {
        return arg;
    }

    public Tab addSubTabs(@NotNull Tab... tabs) {
        for (Tab tab : tabs) {
            addSubTab(tab);
        }

        return this;
    }

    public Tab addSubTab(@NotNull Tab tab) {
        if (hasSubTab(tab)) {
            throw new RuntimeException("已经存在相同 Arg 的 Tab 了");
        }

        subTabMap.put(tab.getArg(), tab);
        return this;
    }

    public boolean hasSubTab(@NotNull Tab tab) {
        return hasSubTab(tab.getArg());
    }

    public boolean hasSubTab(@NotNull String arg) {
        return subTabMap.containsKey(arg);
    }

    public void removeSubTab(@NotNull Tab tab) {
        subTabMap.remove(tab.getArg());
    }

    private StringBuilder toStringBuilder(@NotNull StringBuilder stringBuilder, @NotNull Tab tab, int level) {
        if (level != 0) {
            for (int i = 0; i < level; i++) {
                stringBuilder.append(" ");
            }

            stringBuilder.append("├");
        }

        stringBuilder.append(tab.getArg());

        Collection<Tab> subTabs = tab.getSubTabs();

        if (subTabs != null) {
            for (Tab subTab : subTabs) {
                stringBuilder.append("\n");
                toStringBuilder(stringBuilder, subTab, level + 1);
            }
        }

        return stringBuilder;
    }

    @Override
    public String toString() {
        return toStringBuilder(new StringBuilder(), this, 0).toString();
    }
}
