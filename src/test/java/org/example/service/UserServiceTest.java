package org.example.service;

import org.example.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_returnsTrueForNewValidUser() {
        // Arrange: Create a new User object
        User user = new User(1, "admin1", "pass123", "Admin");

        // Mock userRepository.findByUsername to return null (username doesn't exist yet)
        when(userRepository.findByUsername("admin1")).thenReturn(null);

        // Mock userRepository.saveUser to return true (successful save)
        when(userRepository.saveUser(user)).thenReturn(true);

        // Act: Call registerUser
        boolean result = userService.registerUser(user);

        // Assert: Should return true on successful registration
        // TDD: This test should fail (red) until registerUser is properly implemented
        assertTrue(result);
    }

    @Test
    void registerUser_returnsFalseIfUsernameExists() {
        // Arrange: existing user in DB
        User existing = new User(1, "admin1", "existingHashed", "Admin");
        when(userRepository.findByUsername("admin1")).thenReturn(existing);

        // New user attempting to register with same username
        User newUser = new User(2, "admin1", "pass123", "Admin");

        // Act
        boolean result = userService.registerUser(newUser);

        // Assert: should be false because username already exists
        assertFalse(result);

        // Ensure saveUser was never called
        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void authenticateUser_returnsUserForValidCredentials() throws Exception {
        // Arrange: plain-text password
        String plain = "secret123";

        // Hash the password using SHA-256 to simulate stored hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(plain.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        String hashed = sb.toString();

        // User stored in DB with hashed password
        User stored = new User(1, "user1", hashed, "Staff");
        when(userRepository.findByUsername("user1")).thenReturn(stored);

        // Act: authenticate with plain-text password
        User result = userService.authenticateUser("user1", plain);

        // Assert: should return the user for valid credentials
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }

    @Test
    void authenticateUser_returnsNullForInvalidPassword() throws Exception {
        // Arrange: Hash the correct password 'correct123'
        String correct = "correct123";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(correct.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        String hashed = sb.toString();

        // Stored user has the hashed correct password
        User stored = new User(1, "user2", hashed, "Staff");
        when(userRepository.findByUsername("user2")).thenReturn(stored);

        // Act: attempt authentication with wrong password
        User result = userService.authenticateUser("user2", "wrongPass");

        // Assert: should be null for invalid password
        assertNull(result);
    }

    @Test
    void deleteUser_returnsTrueWhenRequestedByAdmin() {
        // Arrange: admin requesting user
        User admin = new User(1, "adminUser", "hashed", "Admin");
        String targetUsername = "staffUser";

        // Mock repository to return true for deletion
        when(userRepository.deleteUser(targetUsername)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(targetUsername, admin);

        // Assert: should return true when requester is Admin
        // TDD: this test will fail until deleteUser in service is implemented
        assertTrue(result);
    }

    @Test
    void deleteUser_returnsFalseWhenRequestedByStaff() {
        // Arrange: staff requesting user
        User staff = new User(2, "staffUser", "hashed", "Staff");
        String targetUsername = "anyUser";

        // Act
        boolean result = userService.deleteUser(targetUsername, staff);

        // Assert: should return false and repository.deleteUser shouldn't be called
        assertFalse(result);
        verify(userRepository, never()).deleteUser(anyString());
    }

    @Test
    void updateUser_returnsTrueWhenRequestedByAdmin() {
        // Arrange: admin requesting user
        User admin = new User(1, "adminUser", "hashed", "Admin");

        // Updated user data with plain-text password
        User updatedUser = new User(3, "someUser", "newPassword123", "Staff");

        // Mock repository updateUser to return true
        when(userRepository.updateUser(any(User.class))).thenReturn(true);

        // Act
        boolean result = userService.updateUser(updatedUser, admin);

        // Assert: should be true when requester is Admin
        assertTrue(result);
    }

    @Test
    void updateUser_returnsFalseWhenRequestedByStaff() {
        // Arrange: staff requesting user
        User staff = new User(2, "staffUser", "hashed", "Staff");

        // Updated user data
        User updatedUser = new User(4, "otherUser", "newPass", "Staff");

        // Act
        boolean result = userService.updateUser(updatedUser, staff);

        // Assert: should be false and repository.updateUser shouldn't be called
        assertFalse(result);
        verify(userRepository, never()).updateUser(any(User.class));
    }

}