package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @ParameterizedTest
    @ValueSource(strings = {"Admin", "Staff"})
    void setRole_acceptsValidRoles(String role) {
        // construct with a valid role, then change to the tested role
        User user = new User(1, "alice", "secret", "Staff");
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Doctor", "Hacker"})
    void setRole_rejectsInvalidRoles(String role) {
        User user = new User(1, "alice", "secret", "Staff");
        assertThrows(IllegalArgumentException.class, () -> user.setRole(role));
    }
}
