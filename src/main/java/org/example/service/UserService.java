package org.example.service;

import org.example.User;
import org.example.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) {
        return false;
    }
}