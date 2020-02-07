package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.github.julyss2019.mcsp.julylibrary.utils.ArrayUtil;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Deprecated
public class JulyTabCompleter implements org.bukkit.command.TabCompleter {
    private List<String> globalCommands = new ArrayList<>();
    private Map<String, Tab> globalTabMap = new HashMap<>();

    /**
     * 注册
     * @param command
     */
    public void register(JulyTabCommand command) {
        TabCompleter tabCompleter = command.getTabCompleter();

        Validate.notNull(tabCompleter, "JulyTabHandler 的 TabCompleter 不能为 null");
        globalTabMap.putAll(tabCompleter.getTabMap());
        globalCommands.add(command.getFirstArg());
    }

    /**
     * 得到子参数
     * @param cs 发送者
     * @param args 子参数
     * @return
     */
    private List<String> getSubArgs(CommandSender cs, Command command, String label, String... args) {
        List<String> resultArgs = new ArrayList<>();

        // 返回根
        if (args.length == 0) {
            return globalCommands;
        }

        if (!globalTabMap.containsKey(args[0])) {
            return resultArgs;
        }

        Tab tab = globalTabMap.get(args[0]);
        JulyTabCommand tabCommand = tab.getCommand();

        // 权限判断
        if (!cs.hasPermission(tabCommand.getPermission())) {
            return resultArgs;
        }

        // 处理未知（不常规）的Tab
        resultArgs.addAll(Optional.ofNullable(tabCommand.onTabComplete(cs, command, label, args)).orElse(new ArrayList<>()));

        TreeNode<String> lastTreeNode = tab.getNode();

        /*
          遍历得到目标节点
         */
        for (int i = 1; i < args.length; i++) {
            TreeNode<String> tmp = lastTreeNode.find(args[i]);

            if (tmp == null) {
                return resultArgs;
            }

            lastTreeNode = tmp;
        }

        /*
         * 得到最小节点的所有子项
         */
        for (TreeNode<String> node : lastTreeNode.subtrees()) {
            resultArgs.add(node.data());
        }

        return resultArgs;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String label, String[] args) {
        // 过滤第一个无效字符串
        List<String> subArgs = getSubArgs(cs, command, label, ArrayUtil.removeElementFromStrArray(args, args.length - 1)); // 删除最后一个元素
        List<String> resultTabList = new ArrayList<>();

        for (String subArg : subArgs) {
            // 如果前缀匹配了则添加到列表
            if (subArg.startsWith(args[args.length - 1])) {
                resultTabList.add(subArg);
            }
        }

//        // 一个前缀都没匹配
//        if (resultTabList.size() == 0) {
//            resultTabList.addAll(subArgs);
//        }

        return resultTabList.size() == 0 ? null : resultTabList;
    }
}
