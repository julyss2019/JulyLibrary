package com.github.julyss2019.mcsp.julylibrary.message;


import com.github.julyss2019.mcsp.julylibrary.utils.StrUtil;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * Title 构造类
 */
public class TitleBuilder {
    private TitleType titleType = TitleType.TITLE;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    private boolean colored = false;


    public TitleBuilder() {}

    public TitleBuilder colored() {
        colored(true);
        return this;
    }

    public TitleBuilder colored(boolean b) {
        this.colored = b;
        return this;
    }

    public TitleBuilder text(@NotNull String text) {
        this.text = text;
        return this;
    }

    public TitleBuilder type(TitleType titleType) {
        this.titleType = titleType;
        return this;
    }

    public TitleBuilder fadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public TitleBuilder stay(int stay) {
        this.stay = stay;
        return this;
    }

    public TitleBuilder fadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public Title build() {
        return new Title(titleType, colored ? ChatColor.translateAlternateColorCodes('&', text) : text, fadeIn, stay, fadeOut);
    }
}
