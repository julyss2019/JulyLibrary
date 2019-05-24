package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;


public class JulyTabCompleter implements org.bukkit.command.TabCompleter {
    private Map<String, TabEntry> treeMap = new HashMap<>();

    /**
     * 注册自
     * @param julyTabCommand
     */
    public void register(JulyTabCommand julyTabCommand) {
        for (Map.Entry<String, String[]> entry : julyTabCommand.getTabCompleterMap().entrySet()) {
            setSubArgs(julyTabCommand, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 得到子参数
     * @param cs 发送者
     * @param args 子参数
     * @return
     */
    private List<String> getSubArgs(CommandSender cs, String... args) {
        List<String> subArgs = new ArrayList<>();

        // 返回根
        if (args.length == 0 || (args.length == 1 && args[0].equals(""))) {
            // 返回所有根
            for (Map.Entry<String, TabEntry> entry : treeMap.entrySet()) {
                JulyTabCommand julyTabCommand = entry.getValue().getJulyTabCommand();

                // 权限判断
                if (cs.hasPermission(julyTabCommand.getPermission())) {
                    subArgs.add(entry.getKey());
                }
            }

            return subArgs;
        }

        if (!treeMap.containsKey(args[0])) {
            return subArgs;
        }

        TabEntry tabEntry = treeMap.get(args[0]);
        JulyTabCommand julyTabCommand = tabEntry.getJulyTabCommand();

        // 权限判断
        if (!cs.hasPermission(julyTabCommand.getPermission())) {
            return subArgs;
        }

        TreeNode<String> lastTreeNode = tabEntry.getNode();

        /*
          遍历得到最小的 Node
         */
        for (int i = 1; i < args.length; i++) {
            TreeNode<String> tmp = lastTreeNode.find(args[i]);

            if (tmp == null) {
                return subArgs;
            }

            lastTreeNode = tmp;
        }

        /*
         * 得到最小Node的所有子项
         */
        for (TreeNode<String> node : lastTreeNode.subtrees()) {
            subArgs.add(node.data());
        }

        return subArgs;
    }

    /**
     * 设置节点
     * @param julyTabCommand
     * @param parentArgPath
     * @param subArgs
     */
    private void setSubArgs(JulyTabCommand julyTabCommand, String parentArgPath, String... subArgs) {
        String[] pathArray = parentArgPath.split("\\.");

        if (pathArray.length < 1) {
            throw new IllegalArgumentException("路径中没有根");
        }


        TreeNode<String> lastTreeNode = null;

        /*
         *  递归得到最小的节点
         */
        for (int i = 0; i < pathArray.length; i++) {
            if (i == 0) {
                if (!treeMap.containsKey(pathArray[0])) {
                    // 创建根节点
                    treeMap.put(pathArray[0], new TabEntry(julyTabCommand, new ArrayMultiTreeNode<>(pathArray[0])));
                }

                lastTreeNode = treeMap.get(pathArray[0]).getNode();
                continue;
            }

            TreeNode<String> tmp = lastTreeNode.find(pathArray[i]);

            // 没有节点则创建节点
            if (tmp == null) {
                tmp = new ArrayMultiTreeNode<>(pathArray[i]);

                lastTreeNode.add(tmp);
            }

            lastTreeNode = tmp;
        }

        if (subArgs != null) {
            for (String subArg : subArgs) {
                // 除重
                if (lastTreeNode.find(subArg) == null) {
                    lastTreeNode.add(new ArrayMultiTreeNode<>(subArg));
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String label, String[] args) {
        List<String> parentTabList = getSubArgs(cs, ArrayUtil.removeElementFromStrArray(args, args.length - 1)); // 删除最后一个元素
        List<String> resultTabList = new ArrayList<>();

        for (String parentTab : parentTabList) {
            // 如果前缀匹配了则添加到列表
            if (parentTab.startsWith(args[args.length - 1])) {
                resultTabList.add(parentTab);
            }
        }

        if (resultTabList.size() == 0) {
            resultTabList.addAll(parentTabList);
        }

        return resultTabList.size() == 0 ? null : resultTabList;
    }
}
