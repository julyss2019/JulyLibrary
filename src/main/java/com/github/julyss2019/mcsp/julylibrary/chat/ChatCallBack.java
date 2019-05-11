package com.github.julyss2019.mcsp.julylibrary.chat;

public abstract class ChatCallBack {
    private String message;

    public abstract void onCallback();

    void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
