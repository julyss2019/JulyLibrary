package com.github.julyss2019.mcsp.julylibrary.inventory;

import com.github.julyss2019.mcsp.julylibrary.JulyLibrary;
import com.github.julyss2019.mcsp.julylibrary.utils.MessageUtil;
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
    private static JulyLibrary plugin = JulyLibrary.getInstance();
    private static BukkitInventoryListener bukkitInventoryListener = new BukkitInventoryListener();
    private Inventory inventory;
    private int rowCount = -1;
    private String title;
    private Map<Integer, ItemStack> itemIndexMap = new HashMap<>(); // 物品索引表
    private Map<Integer, ItemListener> itemListenerMap = new HashMap<>(); // 物品点击回调表
    private boolean isColored;
    private InventoryListener inventoryListener;
    private boolean cancelClick = true; // 取消点击事件

    public static void init() {
        Bukkit.getPluginManager().registerEvents(bukkitInventoryListener, plugin); // 注册监听器
    }

    public static BukkitInventoryListener getBukkitInventoryListener() {
        return bukkitInventoryListener;
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder() {}

    public InventoryBuilder cancelClick(boolean b) {
        this.cancelClick = b;
        return this;
    }

    public InventoryBuilder fillAll(@NotNull ItemStack itemStack) {
        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置!");
        }

        for (int i = 0; i < rowCount * 9; i++) {
            item(i, itemStack);
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
        return item(index, item, null);
    }

    public InventoryBuilder item(int index, @NotNull ItemStack item, @Nullable ItemListener itemListener) {
        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置!");
        }

        if (index < 0 || index > rowCount * 9) {
            throw new IllegalArgumentException("索引不合法!");
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
        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置!");
        }

        if (row < 0) {
            throw new IllegalArgumentException("row 必须 >= 0.");
        }

        if (row >= this.rowCount) {
            throw new IllegalArgumentException("row 超过了限定的行数.");
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
    public InventoryBuilder item(int row, int column, @NotNull ItemStack item, @Nullable ItemListener itemListener) {
        item(row * 9 + column, item, itemListener);
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
        if (rowCount == -1) {
            throw new IllegalArgumentException("行数未设置!");
        }

        if (inventory == null) {
            this.inventory = Bukkit.createInventory(null, rowCount * 9, this.isColored ? MessageUtil.translateColorCode(title) : title);
        }

        // 设置物品
        for (Map.Entry<Integer, ItemStack> entry : itemIndexMap.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());

        }

        if (!cancelClick && (itemListenerMap.size() > 0 || inventoryListener != null)) {
            throw new IllegalArgumentException("用监听物品或背包 cancelClick 必须为 true.");
        }

        // 注册监听的物品
        if (itemListenerMap.size() > 0) {
            List<ListenerItem> listenerItems = new ArrayList<>();

            for (Map.Entry<Integer, com.github.julyss2019.mcsp.julylibrary.inventory.ItemListener> entry : itemListenerMap.entrySet()) {
                listenerItems.add(new ListenerItem(entry.getKey(), entry.getValue()));
            }

            bukkitInventoryListener.registerListenerItems(inventory, listenerItems);
        }

        // 注册监听的背包
        if (inventoryListener != null) {
            bukkitInventoryListener.registerInventoryListener(inventory, inventoryListener);
        }

        // 注册取消点击的背包
        if (cancelClick) {
            bukkitInventoryListener.registerCancelClickInventory(inventory);
        }

        return inventory;
    }
}
