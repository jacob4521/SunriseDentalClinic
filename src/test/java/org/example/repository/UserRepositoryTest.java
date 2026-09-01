package org.example.repository;

import org.example.User;
import org.junit.jupiter.api.BeforeEach; // අලුතින් එකතු වූ import
import org.junit.jupiter.api.Test;

import java.sql.Connection; // අලුතින් එකතු වූ import
import java.sql.DriverManager; // අලුතින් එකතු වූ import
import java.sql.Statement; // අලුතින් එකතු වූ import

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserRepositoryTest {

    // හැම Test එකක්ම ධාවනය වෙන්න කලින් මේක ස්වයංක්‍රීයව Run වෙනවා
    @BeforeEach
    void setUpDatabase() throws Exception {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
        try (Connection conn = DriverManager.getConnection(url, "sa", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("CREATE TABLE users (" +
                    "userId INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL)");
        }
    }

    @Test
    void saveUser_returnsTrueForValidUser() {
        UserRepository repo = new UserRepository("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        String uniqueUsername = "bob_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "pass", "Staff");

        assertTrue(repo.saveUser(user));
    }

    @Test
    void findByUsername_returnsSavedUser() {
        UserRepository repo = new UserRepository("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        String uniqueUsername = "alice_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "secret", "Staff");

        repo.saveUser(user);

        User found = repo.findByUsername(uniqueUsername);

        assertNotNull(found);
        assertEquals(uniqueUsername, found.getUsername());
    }

    @Test
    void updateUser_updatesUserSuccessfully() {
        UserRepository repo = new UserRepository("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        String uniqueUsername = "charlie_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "originalPassword", "Staff");

        repo.saveUser(user);

        user.setPassword("updatedPassword");
        user.setRole("Admin");

        assertTrue(repo.updateUser(user));

        User updatedUser = repo.findByUsername(uniqueUsername);

        assertNotNull(updatedUser);
        assertEquals(uniqueUsername, updatedUser.getUsername());
        assertEquals("updatedPassword", updatedUser.getPassword());
        assertEquals("Admin", updatedUser.getRole());
    }

    @Test
    void deleteUser_deletesUserSuccessfully() {
        UserRepository repo = new UserRepository("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        String uniqueUsername = "diana_" + System.currentTimeMillis();
        User user = new User(1, uniqueUsername, "tempPassword", "Staff");

        repo.saveUser(user);

        assertTrue(repo.deleteUser(uniqueUsername));

        User deletedUser = repo.findByUsername(uniqueUsername);

        assertNull(deletedUser);
    }
}