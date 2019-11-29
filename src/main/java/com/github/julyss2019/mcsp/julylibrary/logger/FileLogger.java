package com.github.julyss2019.mcsp.julylibrary.logger;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private static final SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm:ss");

    public enum LoggerLevel {DEBUG, ERROR, INFO, WARNING}

    private File loggerFolder;
    private String fileName;
    private boolean autoFlush;

    private File loggerFile;
    private FileWriter loggerWriter;
    private BufferedWriter loggerBufferedWriter;

    private static String escape(String input) {
        StringBuilder sb = new StringBuilder();
        int max = input.length();
        int i;

        for (i = 0; i < max; i++) {
            char c = input.charAt(i);

            if (i + 1 >= max) {
                sb.append(c);
                continue;
            }

            if (c == '%') {
                char next = input.charAt(i + 1);

                switch (next) {
                    case 'd':
                        int left = input.indexOf("{", i + 1);

                        if (left == -1) {
                            throw new RuntimeException("非法的表达式");
                        }

                        int right = input.indexOf("}", left);

                        if (right == -1) {
                            throw new RuntimeException("非法的表达式");
                        }

                        sb.append(new SimpleDateFormat(input.substring(left + 1, right)).format(new Date()));
                        i = right;
                        break;
                    case '%':
                        sb.append("%");
                        i++;
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            } else {
                sb.append(c);
            }

        }

        return sb.toString();
    }

    private FileLogger(Builder builder) {
        loggerFolder = builder.loggerFolder;
        fileName = builder.fileName;
        autoFlush = builder.autoFlush;
    }

    public File getLoggerFolder() {
        return loggerFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isAutoFlush() {
        return autoFlush;
    }

    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    public void setLoggerFolder(@NotNull File loggerFolder) {
        this.loggerFolder = loggerFolder;
        checkFile();
    }

    public void setFileName(@NotNull String fileName) {
        this.fileName = fileName;
        checkFile();
    }

    /**
     * 刷新，写入内容到文件中
     */
    public void flush() {
        // 写入
        try {
            loggerBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查文件名变更
     */
    private void checkFile() {
        String newFileName = escape(fileName);

        if (loggerFile == null || !newFileName.equalsIgnoreCase(loggerFile.getName())) {
            close();
            // 重新定义新的文件
            this.loggerFile = new File(this.loggerFolder, newFileName);

            try {
                this.loggerWriter = new FileWriter(this.loggerFile, true);
                this.loggerBufferedWriter = new BufferedWriter(this.loggerWriter);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("创建 Writer 失败");
            }
        }
    }

    /**
     * DEBUG
     * @param s
     */
    public void d(String s) {
        log(LoggerLevel.DEBUG, s);
    }

    /**
     * ERROR
     * @param s
     */
    public void e(String s) {
        log(LoggerLevel.ERROR, s);
    }

    /**
     * WARNING
     * @param s
     */
    public void w(String s) {
        log(LoggerLevel.WARNING, s);
    }

    /**
     * INFO
     * @param s
     */
    public void i(String s) {
        log(LoggerLevel.INFO, s);
    }

    private void log(@NotNull LoggerLevel loggerLevel, @NotNull String s) {
        String log = "[" + loggerLevel.name() + "] " + "[" + TIME_SDF.format(System.currentTimeMillis()) + "] " + s;

        write(log);
    }

    /**
     * 写入内容到缓存中
     * @param s
     * @return
     */
    public void write(String s) {
        checkFile();

        try {
            this.loggerBufferedWriter.write(s);
            this.loggerBufferedWriter.newLine(); // 换行
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (autoFlush) {
            flush();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            this.loggerBufferedWriter.flush();
            this.loggerWriter.flush();
            this.loggerBufferedWriter.close();
            this.loggerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final class Builder {
        private File loggerFolder;
        private String fileName;
        private boolean autoFlush;

        public Builder() {}

        public Builder loggerFolder(@NotNull File loggerFolder) {
            this.loggerFolder = loggerFolder;
            return this;
        }

        public Builder fileName(@NotNull String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder autoFlush(boolean autoFlush) {
            this.autoFlush = autoFlush;
            return this;
        }

        public FileLogger build() {
            return new FileLogger(this);
        }
    }
}
