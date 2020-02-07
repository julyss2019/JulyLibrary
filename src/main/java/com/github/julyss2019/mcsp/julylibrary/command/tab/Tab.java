package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.scalified.tree.multinode.ArrayMultiTreeNode;

@Deprecated
class Tab {
    private JulyTabCommand command;
    private ArrayMultiTreeNode<String> node;

    Tab(JulyTabCommand command, ArrayMultiTreeNode<String> node) {
        this.command = command;
        this.node = node;
    }

    /**
     * 得到节点
     * @return
     */
    ArrayMultiTreeNode<String> getNode() {
        return node;
    }

    /**
     * 得到 JulyTabHandler
     * @return
     */
    JulyTabCommand getCommand() {
        return command;
    }
}
