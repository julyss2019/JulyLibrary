package com.github.julyss2019.mcsp.julylibrary.message;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * 可通过 TitleBuilder 构造出本类
 */
public class Title {
    public enum Type {TITLE, SUBTITLE}

    private Title.Type titleType;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    protected Title(Title.Type type, String text, int fadeIn, int stay, int fadeOut) {
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

    public static class Builder {
        private Title.Type titleType = Title.Type.TITLE;
        private String text;
        private int fadeIn;
        private int stay;
        private int fadeOut;
        private boolean colored = false;


        public Builder() {}

        public Builder colored() {
            colored(true);
            return this;
        }

        public Builder colored(boolean b) {
            this.colored = b;
            return this;
        }

        public Builder text(@NotNull String text) {
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
