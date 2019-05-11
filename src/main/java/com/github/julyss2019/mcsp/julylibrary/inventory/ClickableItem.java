package com.github.julyss2019.mcsp.julylibrary.inventory;

public class ClickableItem {
    private int index;
    private ClickedCallback clickedCallback;

    public ClickableItem(int index, ClickedCallback clickedCallback) {
        this.index = index;
        this.clickedCallback = clickedCallback;
    }

    public int getIndex() {
        return index;
    }

    public ClickedCallback getClickedCallback() {
        return clickedCallback;
    }
}
