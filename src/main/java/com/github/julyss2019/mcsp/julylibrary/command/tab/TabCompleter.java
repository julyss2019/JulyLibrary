package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.scalified.tree.multinode.ArrayMultiTreeNode;

public class TabCompleter {
    private TabCommand tabCommand;
    private ArrayMultiTreeNode<String> node;

    protected TabCompleter(TabCommand tabCommand, ArrayMultiTreeNode<String> node) {
        this.tabCommand = tabCommand;
        this.node = node;
    }

    /**
     * 得到节点
     * @return
     */
    protected ArrayMultiTreeNode<String> getNode() {
        return node;
    }

    /**
     * 得到 TabCommand
     * @return
     */
    protected TabCommand getTabCommand() {
        return tabCommand;
    }
}
