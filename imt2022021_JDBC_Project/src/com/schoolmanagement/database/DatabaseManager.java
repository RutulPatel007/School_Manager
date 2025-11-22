package com.schoolmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/school_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "admin";

    private DatabaseManager() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    public static DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
