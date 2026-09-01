package org.example.repository;

import org.example.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void findByUsername_returnsSavedUser() {
        UserRepository repo = new UserRepository();
        String uniqueUsername = "alice_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "secret", "Staff");

        // Attempt to save user first (may fail if DB not configured) - TDD expects findByUsername to be unimplemented and test to fail (red)
        repo.saveUser(user);

        User found = repo.findByUsername(uniqueUsername);

        // Strict TDD: method currently returns null so this test should fail (red)
        assertNotNull(found);
        assertEquals(uniqueUsername, found.getUsername());
    }

    @Test
    void updateUser_updatesUserSuccessfully() {
        UserRepository repo = new UserRepository();
        String uniqueUsername = "charlie_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "originalPassword", "Staff");

        // Save user first
        repo.saveUser(user);

        // Modify the user's password and role
        user.setPassword("updatedPassword");
        user.setRole("Admin");

        // Call updateUser - should return true on successful update
        // TDD: This test should fail (red) until updateUser is properly implemented
        assertTrue(repo.updateUser(user));

        // Fetch the user again to verify the changes were persisted
        User updatedUser = repo.findByUsername(uniqueUsername);

        assertNotNull(updatedUser);
        assertEquals(uniqueUsername, updatedUser.getUsername());
        assertEquals("updatedPassword", updatedUser.getPassword());
        assertEquals("Admin", updatedUser.getRole());
    }
}
