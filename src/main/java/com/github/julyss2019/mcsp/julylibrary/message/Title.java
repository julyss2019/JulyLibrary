package com.github.julyss2019.mcsp.julylibrary.message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 可通过 Title.Builder 构造出本类
 */
public class Title {
    public enum Type {TITLE, SUBTITLE, ACTIONBAR}

    private Title.Type titleType;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    private Title(@NotNull Title.Type type, @NotNull String text, int fadeIn, int stay, int fadeOut) {
        this.titleType = type;
        this.text = text;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public Title.Type getTitleType() {
        return titleType;
    }

    public String getText() {
        return text;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    /**
     * 发送 Title
     * @param player
     */
    public void send(@NotNull  Player player) {
        JulyMessage.sendTitle(player, this);
    }

    /**
     * Builder
     */
    public static class Builder {
        private Title.Type titleType = Title.Type.TITLE;
        private String text;
        private int fadeIn;
        private int stay;
        private int fadeOut;
        private boolean colored = true;

        public Builder() {}

        public Builder colored() {
            colored(true);
            return this;
        }

        public Builder colored(boolean b) {
            this.colored = b;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder type(Title.Type titleType) {
            this.titleType = titleType;
            return this;
        }

        public Builder fadeIn(int fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        public Builder stay(int stay) {
            this.stay = stay;
            return this;
        }

        public Builder fadeOut(int fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        public Title build() {
            return new Title(Title.Builder.this.titleType, colored ? ChatColor.translateAlternateColorCodes('&', text) : text, fadeIn, stay, fadeOut);
        }
    }
}
