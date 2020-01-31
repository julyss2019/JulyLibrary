package com.github.julyss2019.mcsp.julylibrary.inventory;

public class Item {
    private int index;
    private ItemListener itemListener;

    public Item(int index, ItemListener itemListener) {
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
