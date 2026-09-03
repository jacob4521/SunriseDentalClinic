package org.example;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.setRole(role);
    }

    // Default constructor (Gson වැනි Libraries සඳහා අනිවාර්ය වේ)
    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.isEmpty() || (!"Admin".equals(role) && !"Staff".equals(role))) {
            throw new IllegalArgumentException("Role must be exactly Admin or Staff.");
        }

        this.role = role;
    }
}
