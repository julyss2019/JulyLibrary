package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;


public class JulyTabCompleter implements org.bukkit.command.TabCompleter {
    private HashMap<String, TabCompleter> treeMap = new HashMap<>();

    public JulyTabCompleter() {}

    /**
     * 注册自动完成器
     * @param tabCommand
     */
    public void register(TabCommand tabCommand) {
        for (Map.Entry<String, String[]> entry : tabCommand.getTabCompleterMap().entrySet()) {
            setSubArgs(tabCommand, entry.getKey(), entry.getValue());
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
            for (Map.Entry<String, TabCompleter> entry : treeMap.entrySet()) {
                TabCommand tabCommand = entry.getValue().getTabCommand();

                // 权限判断
                if (cs.hasPermission(tabCommand.getPermission())) {
                    subArgs.add(entry.getKey());
                }
            }

            return subArgs;
        }

        if (!treeMap.containsKey(args[0])) {
            return subArgs;
        }

        TabCompleter tabCompleter = treeMap.get(args[0]);
        TabCommand tabCommand = tabCompleter.getTabCommand();

        // 权限判断
        if (!cs.hasPermission(tabCommand.getPermission())) {
            return subArgs;
        }

        TreeNode<String> lastTreeNode = tabCompleter.getNode();

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
     * @param tabCommand
     * @param parentArgPath
     * @param subArgs
     */
    private void setSubArgs(TabCommand tabCommand, String parentArgPath, String... subArgs) {
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
                    treeMap.put(pathArray[0], new TabCompleter(tabCommand, new ArrayMultiTreeNode<>(pathArray[0])));
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

        // 进行匹配
        for (String parentTab : parentTabList) {
            if (parentTab.startsWith(args[args.length - 1])) {
                resultTabList.add(parentTab);
            }
        }

        if (resultTabList.size() == 0) {
            return null; // 返回null则使用默认的tab规则（一般返回玩家列表）
        }

        return resultTabList;
    }
}
