package com.github.julyss2019.mcsp.julylibrary.logger;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLogger {
    private static SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat TIME_SDF = new SimpleDateFormat("HH:mm:ss");
    private static Gson gson = new Gson();

    private File loggerFolder;
    private String fileNameFormat;
    private int saveInterval;

    private File loggerFile;
    private FileWriter loggerWriter;
    private BufferedWriter loggerBufferedWriter;
    private long nextDayMillis;
    private boolean isDateFormat;

    public enum LoggerLevel {DEBUG, ERROR, INFO, WARNING}

    FileLogger(File loggerFolder, String fileNameFormat, int saveInterval) {
        this.loggerFolder = loggerFolder;
        this.fileNameFormat = fileNameFormat;
        this.saveInterval = saveInterval;
        this.isDateFormat = fileNameFormat.contains("%DATE%");

        // 创建文件
        if (!loggerFolder.exists()) {
            loggerFolder.mkdirs();
        }

        // 初始化
        checkDay();
    }

    /**
     * 刷新，写入内容到文件中
     */
    public void flush() {
        if (!loggerFile.exists()) {
            throw new RuntimeException("文件不存在");
        }

        // 写入
        try {
            loggerBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否到了下一天
     */
    private void checkDay() {
        if (System.currentTimeMillis() >= nextDayMillis) {
            try {
                // 保存旧的
                if (loggerBufferedWriter != null) {
                    loggerBufferedWriter.flush();
                    loggerBufferedWriter.close();
                    loggerWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 得到下一天的时间
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DATE, 1);

            // 重新定义新的文件
            this.loggerFile = new File(this.loggerFolder, (this.fileNameFormat == null ? "%DATE%" : this.fileNameFormat)
                    .replace("%DATE%", DATE_SDF.format(System.currentTimeMillis())));
            this.nextDayMillis = calendar.getTimeInMillis();

            try {
                this.loggerWriter = new FileWriter(this.loggerFile, true);
                this.loggerBufferedWriter = new BufferedWriter(this.loggerWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeJson(Object object) {
        write(gson.toJson(object));
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

    public void log(@NotNull LoggerLevel loggerLevel, @NotNull String s) {
        String log = "[" + loggerLevel.name() + "] " + "[" + TIME_SDF.format(System.currentTimeMillis()) + "] " + s;

        write(log);
    }

    /**
     * 写入内容到缓存中
     * @param s
     * @return
     */
    private boolean write(String s) {
        if (isDateFormat) {
            checkDay();
        }

        try {
            this.loggerBufferedWriter.write(s);
            this.loggerBufferedWriter.newLine(); // 换行
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 关闭
     */
    void close() {
        try {
            this.loggerBufferedWriter.flush();
            this.loggerBufferedWriter.close();
            this.loggerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getLoggerFolder() {
        return loggerFolder;
    }

    public String getFileNameFormat() {
        return fileNameFormat;
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public FileWriter getLoggerWriter() {
        return loggerWriter;
    }

    public BufferedWriter getLoggerBufferedWriter() {
        return loggerBufferedWriter;
    }

    public long getNextDayMillis() {
        return nextDayMillis;
    }

    public File getLoggerFile() {
        return loggerFile;
    }
}
