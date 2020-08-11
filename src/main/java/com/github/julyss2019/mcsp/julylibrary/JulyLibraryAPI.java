package com.github.julyss2019.mcsp.julylibrary;

import com.github.julyss2019.mcsp.julylibrary.chat.ChatInterceptorManager;
import com.github.julyss2019.mcsp.julylibrary.logger.LoggerManager;

public class JulyLibraryAPI {
    public static LoggerManager getLoggerManager() {
        return JulyLibrary.getInstance().getLoggerManager();
    }

    public static ChatInterceptorManager getChatInterceptorManager() {
        return JulyLibrary.getInstance().getChatInterceptorManager();
    }
}
