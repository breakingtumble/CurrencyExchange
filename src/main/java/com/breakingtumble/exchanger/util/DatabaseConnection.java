package com.breakingtumble.exchanger.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite::resource:currency.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
    }

    // Using singleton pattern here to make an instance of DatabaseConnection util class
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Initializing class for db driver and establishing new connection
    // if previous was closed or there wasn't any connections made.
    private void init() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Using singleton to get connection if it wasn't closed
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                init();
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
