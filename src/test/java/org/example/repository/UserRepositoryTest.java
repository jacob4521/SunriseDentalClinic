package org.example.repository;

import org.example.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {

    @Test
    void saveUser_returnsTrueForValidUser() {
        UserRepository repo = new UserRepository();
        String uniqueUsername = "bob_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "pass", "Staff");

        // Expectation: repository should return true for a valid user
        // This test follows TDD and should fail until saveUser is implemented.
        assertTrue(repo.saveUser(user));
    }
}
