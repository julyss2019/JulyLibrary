package com.github.julyss2019.mcsp.julylibrary.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleSQLManager {
    private String driver;
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public SimpleSQLManager(@NotNull String driver, @NotNull String url) {
        this(driver, url, null, null);
    }

    public SimpleSQLManager(@NotNull String driver, @NotNull String url, @Nullable String user, @Nullable String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public synchronized boolean isConnected() {
        return connection != null;
    }

    public synchronized void disconnect() throws RuntimeException {
        if (connection == null) {
            throw new RuntimeException("未连接");
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("关闭连接失败", e);
        }

        this.connection = null;
    }

    public synchronized void connect() throws RuntimeException {
        if (isConnected()) {
            throw new RuntimeException("已连接");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("驱动初始化失败: " + driver, e);
        }

        try {
            this.connection = (password == null || user == null) ? DriverManager.getConnection(url) : DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("连接失败", e);
        }
    }

    public synchronized void reconnect() throws RuntimeException {
        if (!isConnected()) {
            throw new RuntimeException("未连接");
        }

        disconnect();
        connect();
    }

    public synchronized void executeStatement(@NotNull String s) throws RuntimeException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(s);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("SQL语句执行失败: " + s, e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public synchronized Connection getConnection() {
        return connection;
    }
}
