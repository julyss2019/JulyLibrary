package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryBuilder {
    private static JulyLibrary plugin = JulyLibrary.getInstance();
    private static InventoryListenerCaller inventoryListenerCaller = new InventoryListenerCaller();
    private Inventory inventory;
    private int rowCount = -1;
    private String title;
    private Map<Integer, ItemStack> itemIndexMap = new HashMap<>(); // 物品索引表
    private Map<Integer, ItemListener> itemListenerMap = new HashMap<>(); // 物品点击回调表
    private boolean isColored;
    private InventoryListener inventoryListener;

    public static InventoryListenerCaller getInventoryListenerCaller() {
        return inventoryListenerCaller;
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(inventoryListenerCaller, plugin); // 注册监听器
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder() {}

    /**
     * GUI行数
     * @param row
     * @return
     */
    public InventoryBuilder row(int row) {
        if (row <= 0 || row > 6) {
            throw new IllegalArgumentException("行数不合法.");
        }

        this.rowCount = row;
        return this;
    }

    /**
     * 设置被她
     * @param title
     * @return
     */
    public InventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    public InventoryBuilder item(int index, @NotNull ItemStack item) {
        if (index < 0 || index > 53) {
            throw new IllegalArgumentException("索引不合法!");
        }

        itemIndexMap.put(index, item);
        return this;
    }

    public InventoryBuilder item(int index, @NotNull ItemStack item, @NotNull ItemListener itemListener) {
        if (index < 0 || index > 53) {
            throw new IllegalArgumentException("索引不合法!");
        }

        itemIndexMap.put(index, item);
        itemListenerMap.put(index, itemListener);
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
        if (row < 0) {
            throw new IllegalArgumentException("rowCount 必须 >= 0.");
        }

        if (row >= this.rowCount) {
            throw new IllegalArgumentException("rowCount 超过了限定的行数.");
        }

        if (column < 0) {
            throw new IllegalArgumentException("column 必须 >= 0.");
        }

        if (column >= 9) {
            throw new IllegalArgumentException("column 必须 < 9.");
        }

        this.itemIndexMap.put(row * 9 + column, item);
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
    public InventoryBuilder item(int row, int column, @NotNull ItemStack item, @NotNull ItemListener itemListener) {
        item(row, column, item);
        this.itemListenerMap.put(row * 9 + column, itemListener);
        return this;
    }

    // 1 3 3 9
    /**
     *
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
     * 有色的
     * @return
     */
    public InventoryBuilder colored() {
        this.isColored = true;
        return this;
    }

    /**
     * 是否有色
     * @param b
     * @return
     */
    public InventoryBuilder colored(boolean b) {
        this.isColored = b;
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

    public Map<Integer, ItemStack> getItemIndexMap() {
        return itemIndexMap;
    }

    public Inventory build() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, rowCount * 9, this.isColored ? MessageUtil.translateColorCode(title) : title);
        }

        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置!");
        }

        // 设置物品
        for (Map.Entry<Integer, ItemStack> entry : this.itemIndexMap.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());

        }

        List<ListenerItem> listenerItems = new ArrayList<>();

        // 构造监听物品
        for (Map.Entry<Integer, com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener> entry : this.itemListenerMap.entrySet()) {
            listenerItems.add(new ListenerItem(entry.getKey(), entry.getValue()));
        }

        // 注册监听物品
        inventoryListenerCaller.registerListenerItems(this.inventory, listenerItems);

        if (this.inventoryListener != null) {
            // 注册监听背包
            inventoryListenerCaller.registerInventoryListener(this.inventory, this.inventoryListener);
        }

        return this.inventory;
    }
}
