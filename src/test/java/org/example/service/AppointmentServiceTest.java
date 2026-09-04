package org.example.service;

import org.example.model.Appointment;
import org.example.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAppointment_returnsTrue_whenValidData() {
        // Arrange
        Appointment appointment = new Appointment(1, 10, 2, 3, Date.valueOf("2026-09-15"), Time.valueOf("10:30:00"));
        when(appointmentRepository.addAppointment(any(Appointment.class))).thenReturn(true);

        // Act
        boolean result = appointmentService.addAppointment(appointment);

        // Assert
        assertTrue(result);
        verify(appointmentRepository, times(1)).addAppointment(appointment);
    }

    @Test
    void addAppointment_returnsFalse_whenInvalidPatientId() {
        Appointment invalidAppointment = new Appointment(1, -5, 2, 3, Date.valueOf("2026-09-15"), Time.valueOf("10:30:00"));

        // Act
        boolean result = appointmentService.addAppointment(invalidAppointment);

        // Assert
        assertFalse(result);
        verify(appointmentRepository, never()).addAppointment(any(Appointment.class)); // Repository එකට යන්න කලින් නවතින්න ඕනේ
    }
}