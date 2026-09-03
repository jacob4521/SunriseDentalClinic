package org.example.service;

import org.example.model.Patient;
import org.example.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientRepository);
    }

    @Test
    void addPatient_returnsTrueForValidPatient() {
        // Arrange
        Patient patient = new Patient(1, "John Doe", "123 Main St", "555-1234");
        when(patientRepository.addPatient(any(Patient.class))).thenReturn(true);

        // Act
        boolean result = patientService.addPatient(patient);

        // Assert
        assertTrue(result);
        verify(patientRepository).addPatient(any(Patient.class));
    }

    @Test
    void addPatient_returnsFalseWhenNameIsEmpty() {
        // Arrange
        Patient patient = new Patient(1, "", "123 Main St", "555-1234");

        // Act
        boolean result = patientService.addPatient(patient);

        // Assert
        assertFalse(result);
        verify(patientRepository, never()).addPatient(any(Patient.class));
    }

    @Test
    void getAllPatients_returnsListOfPatients() {
        // Arrange
        Patient patient1 = new Patient(1, "John Doe", "123 Main St", "555-1234");
        Patient patient2 = new Patient(2, "Jane Smith", "456 Oak Ave", "555-5678");
        List<Patient> patientList = Arrays.asList(patient1, patient2);
        when(patientRepository.getAllPatients()).thenReturn(patientList);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getPatientName());
        assertEquals("Jane Smith", result.get(1).getPatientName());
    }

    @Test
    void getPatientById_returnsPatient() {
        // Arrange
        Patient patient = new Patient(1, "John Doe", "123 Main St", "555-1234");
        when(patientRepository.getPatientById(1)).thenReturn(patient);

        // Act
        Patient result = patientService.getPatientById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPatientId());
        assertEquals("John Doe", result.getPatientName());
    }

    @Test
    void updatePatient_returnsTrueForValidPatient() {
        // Arrange
        Patient patient = new Patient(1, "John Doe", "123 Main St", "555-1234");
        when(patientRepository.updatePatient(any(Patient.class))).thenReturn(true);

        // Act
        boolean result = patientService.updatePatient(patient);

        // Assert
        assertTrue(result);
        verify(patientRepository).updatePatient(any(Patient.class));
    }

    @Test
    void updatePatient_returnsFalseWhenContactIsEmpty() {
        // Arrange
        Patient patient = new Patient(1, "John Doe", "123 Main St", null);

        // Act
        boolean result = patientService.updatePatient(patient);

        // Assert
        assertFalse(result);
        verify(patientRepository, never()).updatePatient(any(Patient.class));
    }

    @Test
    void deletePatient_returnsTrueWhenSuccessful() {
        // Arrange
        when(patientRepository.deletePatient(1)).thenReturn(true);

        // Act
        boolean result = patientService.deletePatient(1);

        // Assert
        assertTrue(result);
        verify(patientRepository).deletePatient(1);
    }
}
