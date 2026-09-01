package org.example.service;

import org.example.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
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
}
