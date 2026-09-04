package org.example.controller;

import org.example.model.Appointment;
import org.example.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppointmentServletTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AppointmentServlet appointmentServlet;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void doPost_createsAppointment_whenRoleIsAdminOrStaff() throws Exception {
        // Arrange
        when(request.getAttribute("role")).thenReturn("Admin"); // Role එක Admin ලෙස ලබා දීම

        String jsonPayload = "{\"patientId\":10, \"dentistId\":2, \"treatmentId\":3, \"appointmentDate\":\"2026-09-15\", \"appointmentTime\":\"10:30:00\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonPayload)));

        when(appointmentService.addAppointment(any(Appointment.class))).thenReturn(true);

        // Act
        appointmentServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED); // 201 Created Status
        assertTrue(responseWriter.toString().contains("Appointment created successfully"));
    }

    @Test
    void doPost_returnsForbidden_whenRoleIsNull() throws Exception {
        // Arrange
        when(request.getAttribute("role")).thenReturn(null); // Role එකක් නැති අවස්ථාව

        // Act
        appointmentServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden Status
    }

    @Test
    void doGet_returnsAllAppointments() throws Exception {
        // Arrange
        when(request.getAttribute("role")).thenReturn("Admin");
        List<Appointment> mockList = Arrays.asList(new Appointment());
        when(appointmentService.getAllAppointments()).thenReturn(mockList);

        // Act
        appointmentServlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(appointmentService, times(1)).getAllAppointments();
    }

    @Test
    void doDelete_deletesAppointment_whenRoleIsAdmin() throws Exception {
        // Arrange
        when(request.getAttribute("role")).thenReturn("Admin");
        when(request.getParameter("id")).thenReturn("1");
        when(appointmentService.deleteAppointment(1)).thenReturn(true);

        // Act
        appointmentServlet.doDelete(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(responseWriter.toString().contains("Appointment deleted successfully"));
    }

    @Test
    void doDelete_returnsForbidden_whenRoleIsPatient() throws Exception {
        // Arrange (Patient කෙනෙකුට Delete කරන්න අවසර නැත)
        when(request.getAttribute("role")).thenReturn("Patient");

        // Act
        appointmentServlet.doDelete(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}