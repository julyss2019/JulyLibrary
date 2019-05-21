package com.github.julyss2019.mcsp.julylibrary.logger;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLogger {
    private static SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat TIME_SDF = new SimpleDateFormat("yyyy-MM-dd");
    private File loggerFolder;
    private String fileNameFormat;
    private int saveInterval;

    private FileWriter loggerWriter;
    private BufferedWriter loggerBufferedWriter;
    private long nextDayMillis;

    public enum LoggerLevel {DEBUG, ERROR, INFO, WARNING}

    FileLogger(File loggerFolder, String fileNameFormat, int saveInterval) {
        this.loggerFolder = loggerFolder;
        this.fileNameFormat = fileNameFormat;
        this.saveInterval = saveInterval;

        if (!loggerFolder.exists()) {
            loggerFolder.mkdirs();
        }

        // 刷新
        checkNextDay();
    }

    protected void update() {
        if (saveInterval != 0) {
            checkNextDay();

            // 写入
            try {
                loggerBufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNextDay() {
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

            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            this.nextDayMillis = calendar.getTimeInMillis();
            File loggerFile = new File(loggerFolder, (fileNameFormat == null ? "%DATE%" : fileNameFormat)
                    .replace("%DATE%", DATE_SDF.format(System.currentTimeMillis())));

            try {
                this.loggerWriter = new FileWriter(loggerFile, true);
                this.loggerBufferedWriter = new BufferedWriter(loggerWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void d(String s) {
        log(LoggerLevel.DEBUG, s);
    }

    public void e(String s) {
        log(LoggerLevel.ERROR, s);
    }

    public void w(String s) {
        log(LoggerLevel.WARNING, s);
    }

    public void i(String s) {
        log(LoggerLevel.INFO, s);
    }

    public void log(@NotNull LoggerLevel loggerLevel, @NotNull String s) {
        String log = "[" + loggerLevel.name() + "] " + "[" + TIME_SDF.format(System.currentTimeMillis()) + "] " + s;

        write(log);
    }

    private boolean write(String s) {
        checkNextDay();

        try {
            if (saveInterval != 0) {
                loggerBufferedWriter.append(s);
                loggerBufferedWriter.newLine();
            } else {
                loggerBufferedWriter.write(s);
                loggerBufferedWriter.newLine();
                loggerBufferedWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    protected boolean close() {
        try {
            // 保存旧的
            if (loggerBufferedWriter != null) {
                loggerBufferedWriter.close();
                loggerWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
