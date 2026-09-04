package org.example.repository;

import org.example.model.Treatment;
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

public class TreatmentRepositoryTest {

    private TreatmentRepository treatmentRepository;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        treatmentRepository = new TreatmentRepository(mockConnection);
    }

    @Test
    void testAddTreatment_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Treatment treatment = new Treatment(1, "Teeth Whitening", 5000.00);
        boolean result = treatmentRepository.addTreatment(treatment);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Teeth Whitening");
        verify(mockPreparedStatement, times(1)).setDouble(2, 5000.00);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAllTreatments_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("treatment_id")).thenReturn(1);
        when(mockResultSet.getString("treatment_type")).thenReturn("Teeth Whitening");
        when(mockResultSet.getDouble("price")).thenReturn(5000.00);

        List<Treatment> treatments = treatmentRepository.getAllTreatments();

        assertEquals(1, treatments.size());
        assertEquals(1, treatments.get(0).getTreatmentId());
        assertEquals("Teeth Whitening", treatments.get(0).getTreatmentType());
        assertEquals(5000.00, treatments.get(0).getPrice());
    }
}