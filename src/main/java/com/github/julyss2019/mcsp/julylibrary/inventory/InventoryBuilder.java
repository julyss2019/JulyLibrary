package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryBuilder {
    private Inventory inventory;
    private int rowCount = -1;
    private String title = "";
    private Map<Integer, ItemStack> itemIndexMap = new HashMap<>(); // 物品索引表
    private Map<Integer, ItemListener> itemListenerMap = new HashMap<>(); // 物品点击回调表
    private InventoryListener inventoryListener;
    private boolean colored;
    private boolean cancelInteract = true;

    public InventoryBuilder() {}

    private boolean isValidRowCount(int rowCount) {
        return rowCount > 0 && rowCount < 7;
    }

    public InventoryBuilder cancelInteract(boolean b) {
        this.cancelInteract = b;
        return this;
    }

    /**
     * 填充全部(覆盖)
     * @param itemStack
     * @return
     */
    public InventoryBuilder fillAll(@NotNull ItemStack itemStack) {
        fillAll(itemStack, true);
        return this;
    }

    /**
     * 填充全部
     * @param itemStack
     * @param replace 覆盖
     * @return
     */
    public InventoryBuilder fillAll(@NotNull ItemStack itemStack, boolean replace) {
        if (!isValidRowCount(this.rowCount)) {
            throw new IllegalArgumentException("行数未设置");
        }

        for (int i = 0; i < this.rowCount * 9; i++) {
            if (replace || itemIndexMap.containsKey(i)) {
                item(i, itemStack);
            }
        }

        return this;
    }

    /**
     * GUI行数
     * @param row
     * @return
     */
    public InventoryBuilder row(int row) {
        if (row <= 0 || row > 6) {
            throw new IllegalArgumentException("行数不合法");
        }

        this.rowCount = row;
        return this;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public InventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    public InventoryBuilder item(int index, @NotNull ItemStack item) {
        return item(index, item, null);
    }

    public InventoryBuilder item(int index, @NotNull ItemStack item, @Nullable ItemListener itemListener) {
        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置");
        }

        if (index < 0 || index > rowCount * 9) {
            throw new IllegalArgumentException("索引不合法: " + index);
        }

        itemIndexMap.put(index, item);

        if (itemListener != null) {
            itemListenerMap.put(index, itemListener);
        }

        return this;
    }

    /**
     * 设置物品
     * @param row 行，从0开始
     * @param column 列，从0开始
     * @param item
     * @return
     */
    public InventoryBuilder item(int row, int column, @NotNull ItemStack item) {
        item(row * 9 + column, item);
        return this;
    }

    /**
     * 设置物品
     * @param row
     * @param column
     * @param item
     * @param itemListener 物品监听器
     * @return
     */
    public InventoryBuilder item(int row, int column, @NotNull ItemStack item, @Nullable ItemListener itemListener) {
        item(row * 9 + column, item, itemListener);
        return this;
    }


    /**
     * 创建一个矩形
     * @param row1
     * @param column1
     * @param row2
     * @param column2
     * @param itemStack
     * @return
     */
    public InventoryBuilder item(int row1, int column1, int row2, int column2, @NotNull ItemStack itemStack) {
        for (int row = row1; row <= row2; row++) {
            for (int column = column1; column <= column2; column++) {
                item(row, column, itemStack);
            }
        }

        return this;
    }

    /**
     * 着色
     * @return
     */
    public InventoryBuilder colored() {
        this.colored = true;
        return this;
    }

    /**
     * 着色
     * @param b
     * @return
     */
    public InventoryBuilder colored(boolean b) {
        this.colored = b;
        return this;
    }

    /**
     * 背包监听器
     * @param inventoryListener
     * @return
     */
    public InventoryBuilder listener(InventoryListener inventoryListener) {
        this.inventoryListener = inventoryListener;
        return this;
    }

    public Inventory build() {
        if (this.rowCount == -1) {
            throw new IllegalArgumentException("行数未设置");
        }

        this.inventory = Bukkit.createInventory(null, this.rowCount * 9, this.colored ? JulyMessage.toColoredMessage(this.title) : this.title);

        // 设置物品
        for (Map.Entry<Integer, ItemStack> entry : itemIndexMap.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }

        // 注册监听的物品
        if (this.itemListenerMap.size() > 0) {
            List<Item> items = new ArrayList<>();

            for (Map.Entry<Integer, com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener> entry : this.itemListenerMap.entrySet()) {
                items.add(new Item(entry.getKey(), entry.getValue()));
            }

            JulyLibrary.getInstance().getInventoryEventFirer().listenInventoryItems(this.inventory, items);
        }

        // 注册监听的背包
        if (this.inventoryListener != null) {
            JulyLibrary.getInstance().getInventoryEventFirer().listenInventory(this.inventory, this.inventoryListener);
        }

        if (cancelInteract) {
            JulyLibrary.getInstance().getInventoryEventFirer().cancelInventoryInteract(this.inventory);
        }

        return this.inventory;
    }
}
