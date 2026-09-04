package org.example.controller;

import org.example.model.Treatment;
import org.example.service.TreatmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TreatmentControllerTest {

    private TreatmentController treatmentController;

    @Mock
    private TreatmentService mockTreatmentService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        treatmentController = new TreatmentController(mockTreatmentService);

        responseWriter = new StringWriter();
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoGet_Success() throws Exception {
        when(mockTreatmentService.getAllTreatments()).thenReturn(Arrays.asList(new Treatment(1, "Root Canal", 15000.0)));

        treatmentController.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_SuccessAdmin() throws Exception {
        when(mockRequest.getAttribute("role")).thenReturn("Admin");
        String jsonInput = "{\"treatmentType\": \"Braces\", \"price\": 50000.0}";
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));
        when(mockTreatmentService.addTreatment(any(Treatment.class))).thenReturn(true);

        treatmentController.doPost(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_ForbiddenStaff() throws Exception {
        when(mockRequest.getAttribute("role")).thenReturn("Staff");

        treatmentController.doPost(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(mockTreatmentService, never()).addTreatment(any(Treatment.class));
    }
}