package com.github.julyss2019.mcsp.julylibrary.command.tab;

import com.scalified.tree.multinode.ArrayMultiTreeNode;

class Tab {
    private JulyTabCommand julyTabCommand;
    private ArrayMultiTreeNode<String> node;

    Tab(JulyTabCommand julyTabCommand, ArrayMultiTreeNode<String> node) {
        this.julyTabCommand = julyTabCommand;
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
     * 得到 JulyTabCommand
     * @return
     */
    JulyTabCommand getJulyTabCommand() {
        return julyTabCommand;
    }
}
