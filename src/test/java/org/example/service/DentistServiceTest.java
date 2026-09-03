package org.example.service;

import org.example.model.Dentist;
import org.example.repository.DentistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DentistServiceTest {

    private DentistService dentistService;

    @Mock
    private DentistRepository dentistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dentistService = new DentistService(dentistRepository);
    }

    @Test
    void testAddDentist_ValidDentist_ReturnsTrue() {
        Dentist dentist = new Dentist(1, "Dr. Kamal Perera");
        when(dentistRepository.addDentist(dentist)).thenReturn(true);

        assertTrue(dentistService.addDentist(dentist));
        verify(dentistRepository, times(1)).addDentist(dentist);
    }

    @Test
    void testAddDentist_InvalidName_ReturnsFalse() {
        Dentist dentist = new Dentist(1, ""); // හිස් නමක් යවයි

        assertFalse(dentistService.addDentist(dentist));
        verify(dentistRepository, never()).addDentist(any(Dentist.class)); // Repository එකට නොයන බව තහවුරු කරයි
    }

    @Test
    void testGetAllDentists_ReturnsList() {
        List<Dentist> mockList = Arrays.asList(new Dentist(1, "Dr. Kamal Perera"));
        when(dentistRepository.getAllDentists()).thenReturn(mockList);

        List<Dentist> result = dentistService.getAllDentists();

        assertEquals(1, result.size());
        assertEquals("Dr. Kamal Perera", result.get(0).getDentistName());
    }
}