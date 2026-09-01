package org.example.repository;

import org.example.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private String url;
    private String dbUser;
    private String dbPass;

    // Default constructor for production (MySQL)
    public UserRepository() {
        this.url = "jdbc:mysql://localhost:3306/sunrise_dental";
        this.dbUser = "root";
        this.dbPass = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "";
    }

    // Parameterized constructor for testing (H2)
    public UserRepository(String url, String dbUser, String dbPass) {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPass = dbPass != null ? dbPass : "";
    }

    public boolean saveUser(User user) {
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
            e.printStackTrace();
            return false;
        }
    }

    public User findByUsername(String username) {
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
        if (user == null
                || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()
                || user.getRole() == null || user.getRole().isEmpty()) {
            return false;
        }

        String sql = "UPDATE users SET password = ?, role = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getUsername());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}