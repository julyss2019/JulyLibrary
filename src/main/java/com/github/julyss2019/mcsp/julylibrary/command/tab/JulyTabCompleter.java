package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class JulyTabCompleter implements org.bukkit.command.TabCompleter {
    private Map<String, Tab> treeMap = new HashMap<>();

    /**
     * 注册
     * @param command
     */
    public void register(JulyTabCommand command) {
        TabCompleter tabCompleter = command.getTabCompleter();

        Validate.notNull(tabCompleter, "JulyTabCommand 的 TabCompleter 不能为 null");

        for (Map.Entry<String, String[]> entry : tabCompleter.getTabMap().entrySet()) {
            setSubArgs(tabCompleter.getCommand(), entry.getKey(), entry.getValue());
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
        if (args.length == 0) {
            // 返回所有根
            for (Map.Entry<String, Tab> entry : treeMap.entrySet()) {
                JulyTabCommand julyTabCommand = entry.getValue().getCommand();

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

        Tab tab = treeMap.get(args[0]);

        // 权限判断
        if (!cs.hasPermission(tab.getCommand().getPermission())) {
            return subArgs;
        }

        TreeNode<String> lastTreeNode = tab.getNode();

        /*
          遍历得到目标节点
         */
        for (int i = 1; i < args.length; i++) {
            TreeNode<String> tmp = lastTreeNode.find(args[i]);

            if (tmp == null) {
                return subArgs;
            }

            lastTreeNode = tmp;
        }

        /*
         * 得到最小节点的所有子项
         */
        for (TreeNode<String> node : lastTreeNode.subtrees()) {
            subArgs.add(node.data());
        }

        return subArgs;
    }

    /**
     * 设置节点
     * 通过特定的字符串将其转换成Node
     * @param command
     * @param parentArgPath
     * @param subArgs
     */
    private void setSubArgs(JulyTabCommand command, String parentArgPath, String... subArgs) {
        String[] pathArray = parentArgPath.split("\\.");

        // 如果不存在则创建根节点
        if (!treeMap.containsKey(pathArray[0])) {
            treeMap.put(pathArray[0], new Tab(command, new ArrayMultiTreeNode<>(pathArray[0])));
        }

        // 根节点
        TreeNode<String> treeNode = treeMap.get(pathArray[0]).getNode();

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
