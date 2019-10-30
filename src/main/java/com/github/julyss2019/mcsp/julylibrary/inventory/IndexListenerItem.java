package com.github.julyss2019.mcsp.julylibrary.inventory;

public class IndexListenerItem {
    private int index;
    private ItemListener itemListener;

    public IndexListenerItem(int index, ItemListener itemListener) {
        this.index = index;
        this.itemListener = itemListener;
    }

    public int getIndex() {
        return index;
    }

    public ItemListener getItemListener() {
        return itemListener;
    }
}
