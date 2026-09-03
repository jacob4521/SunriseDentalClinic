package org.example.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil Tests")
public class JwtUtilTest {

    @Test
    @DisplayName("Should generate and validate token with username and role")
    void shouldGenerateAndValidateToken() {
        // Arrange
        String username = "adminUser";
        String role = "Admin";

        // Act
        String token = org.example.util.JwtUtil.generateToken(username, role);

        // Assert - Token is generated and not empty
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");

        // Act - Extract username from token
        String extractedUsername = org.example.util.JwtUtil.extractUsername(token);

        // Assert - Extracted username matches original
        assertEquals(username, extractedUsername, "Extracted username should match the original username");

        // Act - Extract role from token
        String extractedRole = org.example.util.JwtUtil.extractRole(token);

        // Assert - Extracted role matches original
        assertEquals(role, extractedRole, "Extracted role should match the original role");
    }
}
