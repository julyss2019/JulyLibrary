package com.github.julyss2019.mcsp.julylibrary.message;


import org.jetbrains.annotations.NotNull;

/**
 * Title 构造类
 */
public class TitleBuilder {
    private TitleType titleType;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public TitleBuilder(@NotNull TitleType titleType, @NotNull String text) {
        this.titleType = titleType;
        this.text = text;
    }

    public TitleBuilder text(@NotNull String text) {
        this.text = text;
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
        return new Title(titleType, text, fadeIn, stay, fadeOut);
    }
}
