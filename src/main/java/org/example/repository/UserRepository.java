package org.example.repository;

import org.example.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRepository {
    public boolean saveUser(User user) {
        String url = "jdbc:mysql://localhost:3306/sunrise_dental";
        String dbUser = "root";
        String dbPass = System.getenv("DB_PASSWORD"); // prefer environment config for secrets
        if (dbPass == null) dbPass = "";

        // basic input validation to avoid SQL errors / NPEs
        if (user == null
                || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()
                || user.getRole() == null || user.getRole().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            // log the exception to help debugging test failures
            e.printStackTrace();
            return false;
        }
    }

    public User findByUsername(String username) {
        String url = "jdbc:mysql://localhost:3306/sunrise_dental";
        String dbUser = "root";
        String dbPass = System.getenv("DB_PASSWORD");
        if (dbPass == null) dbPass = "";

        if (username == null || username.isEmpty()) {
            return null;
        }

        String sql = "SELECT username, password, role FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            0,
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateUser(User user) {
        return false;
    }
}