package org.example.controller;

import org.example.model.Dentist;
import org.example.service.DentistService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DentistServletTest {

    private DentistServlet dentistServlet;

    @Mock
    private DentistService dentistService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        dentistServlet = new DentistServlet(dentistService);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoGet_ReturnsDentistList() throws Exception {
        when(dentistService.getAllDentists()).thenReturn(Arrays.asList(new Dentist(1, "Dr. Kamal")));

        dentistServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertTrue(responseWriter.toString().contains("Dr. Kamal"));
    }

    @Test
    void testDoPost_AsAdmin_AddsDentistSuccessfully() throws Exception {
        when(request.getAttribute("role")).thenReturn("Admin");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"dentistName\":\"Dr. Silva\"}")));
        when(dentistService.addDentist(any(Dentist.class))).thenReturn(true);

        dentistServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertTrue(responseWriter.toString().contains("successfully"));
    }

    @Test
    void testDoPost_AsStaff_ReturnsForbidden() throws Exception {
        when(request.getAttribute("role")).thenReturn("Staff");

        dentistServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        assertTrue(responseWriter.toString().contains("Access denied"));
    }
}