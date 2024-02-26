package org.example.zboruri.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url;
    private final String username;
    private final String password;
    private static DatabaseConnection instance = null;
    private Connection connection = null;

    public DatabaseConnection(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        connection = DriverManager.getConnection(url, username, password);
    }

    public static void setInstance(String url, String username, String password) throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection(url, username, password);
        }
    }

    public static DatabaseConnection getSingleInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
