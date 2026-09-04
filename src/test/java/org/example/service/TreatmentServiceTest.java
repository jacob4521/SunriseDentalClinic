package org.example.service;

import org.example.model.Treatment;
import org.example.repository.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TreatmentServiceTest {

    private TreatmentService treatmentService;

    @Mock
    private TreatmentRepository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        treatmentService = new TreatmentService(mockRepository);
    }

    @Test
    void testAddTreatment_Success() {
        when(mockRepository.addTreatment(any(Treatment.class))).thenReturn(true);
        Treatment treatment = new Treatment(0, "Root Canal", 15000.00);

        boolean result = treatmentService.addTreatment(treatment);
        assertTrue(result);
        verify(mockRepository, times(1)).addTreatment(treatment);
    }

    @Test
    void testAddTreatment_InvalidData() {
        // Validation: Empty type or negative price should return false without calling the repository
        Treatment invalidTreatment1 = new Treatment(0, "", 5000.00);
        Treatment invalidTreatment2 = new Treatment(0, "Cleaning", -500.00);

        assertFalse(treatmentService.addTreatment(invalidTreatment1));
        assertFalse(treatmentService.addTreatment(invalidTreatment2));

        // Ensure repository was never called
        verify(mockRepository, never()).addTreatment(any(Treatment.class));
    }

    @Test
    void testGetAllTreatments_Success() {
        List<Treatment> mockList = Arrays.asList(new Treatment(1, "Root Canal", 15000.00));
        when(mockRepository.getAllTreatments()).thenReturn(mockList);

        List<Treatment> result = treatmentService.getAllTreatments();
        assertEquals(1, result.size());
        assertEquals("Root Canal", result.get(0).getTreatmentType());
    }
}