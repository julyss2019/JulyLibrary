package com.github.julyss2019.mcsp.julylibrary.utilv2;


import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockUtil {
    /**
     * 得到物品ID
     * @param block
     * @return
     */
    public static String getId(@NotNull Block block) {
        int id = block.getType().getId();
        short data = block.getData();

        return id + (data == 0 ? "" : ":" + data);
    }
}
