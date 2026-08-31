package util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);
    }
}