package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnection {
    // Read database connection details from environment variables with safe local defaults
    private static final Map<String, String> ENV = System.getenv();
    private static final String URL = ENV.getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/sunrise_dental");
    private static final String USER = ENV.getOrDefault("DB_USER", "root");
    private static final String PASSWORD = ENV.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}