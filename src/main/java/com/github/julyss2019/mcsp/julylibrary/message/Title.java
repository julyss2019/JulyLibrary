package com.github.julyss2019.mcsp.julylibrary.message;

/**
 * 可通过 TitleBuilder 构造出本类
 */
public class Title {
    private TitleType titleType;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    protected Title(TitleType titleType, String text, int fadeIn, int stay, int fadeOut) {
        this.titleType = titleType;
        this.text = text;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleType getTitleType() {
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
}
