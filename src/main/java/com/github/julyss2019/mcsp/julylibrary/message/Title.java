package com.github.julyss2019.mcsp.julylibrary.message;

public class Title {
    private TitleType titleType;
    private String text;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(TitleType titleType, String text, int fadeIn, int stay, int fadeOut) {
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
