package ua.edu.ukma.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private final Properties properties;

    public DBConnector(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));
        } catch (SQLException e) {
            System.out.printf("Can't connect to the database %n");
            throw new RuntimeException("Failed to create connection", e);
        }
    }
}
