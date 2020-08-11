package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.text.JulyText;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InventoryBuilder {
    private int row = -1;
    private String title = "";
    private Map<Integer, ItemStack> itemIndexMap = new HashMap<>(); // 物品索引表
    private Map<Integer, ItemListener> itemListenerMap = new HashMap<>(); // 物品点击回调表
    private InventoryListener inventoryListener;
    private boolean colored = true;

    /**
     * 设置GUI行数
     * @param row
     * @return
     */
    public InventoryBuilder row(int row) {
        if (row <= 0 || row > 6) {
            throw new IllegalArgumentException("行数不合法");
        }

        this.row = row;
        return this;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public InventoryBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置物品
     * @param index
     * @param item
     * @return
     */
    public InventoryBuilder item(int index, @Nullable ItemStack item) {
        return item(index, item, null);
    }

    /**
     * 设置物品
     * @param index
     * @param item
     * @param itemListener
     * @return
     */
    public InventoryBuilder item(int index, @Nullable ItemStack item, @Nullable ItemListener itemListener) {
        if (index < 0 || index > 54) {
            throw new RuntimeException("索引不合法: " + index);
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
    public InventoryBuilder item(int row, int column, @Nullable ItemStack item) {
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
    public InventoryBuilder item(int row, int column, @Nullable ItemStack item, @Nullable ItemListener itemListener) {
        item(row * 9 + column, item, itemListener);
        return this;
    }


    /**
     * 创建一个矩形物品
     * @param row1
     * @param column1
     * @param row2
     * @param column2
     * @param itemStack
     * @return
     */
    public InventoryBuilder item(int row1, int column1, int row2, int column2, @Nullable ItemStack itemStack) {
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
    public InventoryBuilder listener(@Nullable InventoryListener inventoryListener) {
        this.inventoryListener = inventoryListener;
        return this;
    }

    private boolean isValidRowCount(int rowCount) {
        return rowCount > 0 && rowCount < 7;
    }

    public Inventory build() {
        if (this.row == -1) {
            throw new IllegalArgumentException("行数未设置");
        }

        itemIndexMap.keySet().forEach(integer -> {
            if (integer >= row * 9) {
                throw new RuntimeException("索引越界: " + integer + ", Size: " + (row * 9));
            }
        });

        Inventory inventory = Bukkit.createInventory(null, this.row * 9, this.colored ? JulyText.getColoredText(this.title) : this.title);

        // 设置物品
        for (Map.Entry<Integer, ItemStack> entry : itemIndexMap.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }

        BuilderInventory builderInventory = new BuilderInventory(inventory);

        // 监听物品
        if (itemListenerMap.size() > 0) {
            Set<BuilderInventory.ListenerItem> listenerItems = new HashSet<>();

            for (Map.Entry<Integer, com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener> entry : this.itemListenerMap.entrySet()) {
                listenerItems.add(new BuilderInventory.ListenerItem(entry.getKey(), entry.getValue()));
            }

            builderInventory.setListenerItems(listenerItems);
        }

        // 背包监听器
        if (inventoryListener != null) {
            builderInventory.setInventoryListener(inventoryListener);
        }

        JulyLibrary.getInstance().getBuilderInventoryManager().registerBuilderInventory(builderInventory); // 注册到 Inventory 管理器中
        return inventory;
    }
}
