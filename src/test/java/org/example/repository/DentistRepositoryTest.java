package org.example.repository;

import org.example.model.Dentist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DentistRepositoryTest {

    private DentistRepository dentistRepository;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        dentistRepository = new DentistRepository(mockConnection);
    }

    @Test
    void testAddDentist_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Dentist dentist = new Dentist(1, "Dr. Kamal Perera");
        boolean result = dentistRepository.addDentist(dentist);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Dr. Kamal Perera");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAllDentists_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("dentist_id")).thenReturn(1);
        when(mockResultSet.getString("dentist_name")).thenReturn("Dr. Kamal Perera");

        List<Dentist> dentists = dentistRepository.getAllDentists();

        assertEquals(1, dentists.size());
        assertEquals(1, dentists.get(0).getDentistId());
        assertEquals("Dr. Kamal Perera", dentists.get(0).getDentistName());
    }
}