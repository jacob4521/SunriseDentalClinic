package org.example.repository;

import org.example.model.Appointment;
import util.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppointmentRepositoryTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private MockedStatic<DatabaseConnection> mockedDatabaseConnection;
    private AppointmentRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        repository = new AppointmentRepository();

        // Mocking the static method DatabaseConnection.getConnection()
        mockedDatabaseConnection = mockStatic(DatabaseConnection.class);
        mockedDatabaseConnection.when(DatabaseConnection::getConnection).thenReturn(mockConnection);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @AfterEach
    void tearDown() {
        // අනිවාර්යයෙන්ම static mock එක close කළ යුතුයි
        mockedDatabaseConnection.close();
    }

    @Test
    void addAppointment_returnsTrue_whenSuccessful() throws SQLException {
        // Arrange
        Appointment appointment = new Appointment(1, 10, 2, 3, Date.valueOf("2026-09-15"), Time.valueOf("10:30:00"));
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 row affected

        // Act
        boolean result = repository.addAppointment(appointment);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, 10); // patient_id
        verify(mockPreparedStatement).setInt(2, 2);  // dentist_id
        verify(mockPreparedStatement).executeUpdate();
    }
}