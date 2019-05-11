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
    private Inventory inventory;
    private int row;
    private String title;
    private Map<Integer, ItemStack> itemIndexMap = new HashMap<>(); // 物品索引表
    private Map<Integer, ClickedCallback> itemCallbackMap = new HashMap<>(); // 物品点击回调表
    private static InventoryListener inventoryListener = new InventoryListener();
    private boolean isColored;

    static {
        Bukkit.getPluginManager().registerEvents(inventoryListener, plugin);
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder() {}

    public InventoryBuilder row(int row) {
        if (row < 0 || row > 5) {
            throw new IllegalArgumentException("行数不合法.");
        }

        this.row = row;
        return this;
    }

    public InventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置物品
     * @param row 行，从0开始
     * @param column 列，从0开始
     * @param itemStack
     * @return
     */
    public InventoryBuilder item(int row, int column, @NotNull ItemStack itemStack) {
        if (row < 0) {
            throw new IllegalArgumentException("row 必须 >= 0.");
        }

        if (row >= this.row) {
            throw new IllegalArgumentException("row 超过了限定的行数.");
        }

        if (column < 0) {
            throw new IllegalArgumentException("column 必须 >= 0.");
        }

        if (column >= 9) {
            throw new IllegalArgumentException("column 必须 < 9.");
        }

        itemIndexMap.put(row * 9 + column, itemStack);
        return this;
    }

    /**
     *
     * @param row
     * @param column
     * @param item
     * @param clickedCallback 点击回调
     * @return
     */
    public InventoryBuilder item(int row, int column, @NotNull ItemStack item, @NotNull ClickedCallback clickedCallback) {
        item(row, column, item);
        itemCallbackMap.put(row * 9 + column, clickedCallback);
        return this;
    }

    public InventoryBuilder colored() {
        this.isColored = true;
        return this;
    }

    public InventoryBuilder colored(boolean b) {
        this.isColored = b;
        return this;
    }

    public Inventory build() {
        if (inventory == null) {
            if (row == 0) {
                throw new IllegalArgumentException("row 未设置.");
            }

            this.inventory = Bukkit.createInventory(null, row * 9, this.isColored ? MessageUtil.translateColorCode(title) : title);
        }

        for (Map.Entry<Integer, ItemStack> entry : this.itemIndexMap.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }

        List<ClickableItem> clickableItems = new ArrayList<>();

        for (Map.Entry<Integer, ClickedCallback> entry : this.itemCallbackMap.entrySet()) {
            clickableItems.add(new ClickableItem(entry.getKey(), entry.getValue()));
        }

        inventoryListener.registerItems(this.inventory, clickableItems);
        return inventory;
    }
}
