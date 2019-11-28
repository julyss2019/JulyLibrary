package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TabCompleter {
    private JulyTabCommand command;
    private Map<String, Tab> tabMap;

    public TabCompleter(JulyTabCommand command, Map<String, Tab> tabMap) {
        this.command = command;
        this.tabMap = tabMap;
    }

    public Map<String, Tab> getTabMap() {
        return (Map<String, Tab>) ((HashMap) tabMap).clone();
    }

    public JulyTabCommand getCommand() {
        return command;
    }

    public static class Builder {
        private JulyTabCommand command;
        private Map<String, Tab> tabMap = new HashMap<>();

        public Builder command(@NotNull JulyTabCommand command) {
            this.command = command;
            return this;
        }

        public Builder create(String... paths) {
            for (String path : paths) {
                set(path);
            }

            return this;
        }

        public Builder set(String path, String... values) {
            if (command == null) {
                throw new RuntimeException("command 未设置.");
            }

            setSubArgs(command.getFirstArg() + "." + path, values);
            return this;
        }

        /**
         * 设置节点
         * 通过特定的字符串将其转换成Node
         * @param parentArgPath
         * @param subArgs
         */
        private void setSubArgs(String parentArgPath, String... subArgs) {
            String[] pathArray = parentArgPath.split("\\.");

            // 如果不存在则创建根节点
            if (!tabMap.containsKey(pathArray[0])) {
                tabMap.put(pathArray[0], new Tab(command, new ArrayMultiTreeNode<>(pathArray[0])));
            }

            // 根节点
            TreeNode<String> treeNode = tabMap.get(pathArray[0]).getNode();

            /*
             *  遍历得到的目标节点
             */
            for (String s : pathArray) {
                TreeNode<String> tmp = treeNode.find(s);

                // 没有节点则创建节点
                if (tmp == null) {
                    tmp = new ArrayMultiTreeNode<>(s);

                    treeNode.add(tmp);
                }

                treeNode = tmp;
            }

            // 此时 node 已经是目标节点了
            for (String subArg : subArgs) {
                if (subArg == null) {
                    continue;
                }

                if (treeNode.find(subArg) == null) {
                    treeNode.add(new ArrayMultiTreeNode<>(subArg));
                }
            }
        }

        public TabCompleter build() {
            if (command == null) {
                throw new RuntimeException("command 未设置.");
            }

            return new TabCompleter(command, tabMap);
        }
    }
}
