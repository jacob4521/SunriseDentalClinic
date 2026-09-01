package org.example.service;

import org.example.User;
import org.example.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            return false;
        }

        // Check if username already exists
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            return false;
        }

        // Hash the user's password with SHA-256
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashed) {
                hex.append(String.format("%02x", b));
            }
            user.setPassword(hex.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Save the user and return result
        return userRepository.saveUser(user);
    }

    public User authenticateUser(String username, String password) {
        return null;
    }
}