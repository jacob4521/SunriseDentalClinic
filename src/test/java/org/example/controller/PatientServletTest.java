package org.example.controller;

import org.example.model.Patient;
import org.example.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private PatientService patientService;

    private PatientServlet patientServlet;

    @BeforeEach
    void setUp() {
        patientServlet = new PatientServlet(patientService);
    }

    @Test
    void doPost_createsPatientSuccessfully() throws Exception {
        when(req.getAttribute("role")).thenReturn("Admin");

        String json = "{\"patientName\":\"John Doe\",\"address\":\"Galle\",\"contactNumber\":\"0712345678\"}";
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(patientService.addPatient(any(Patient.class))).thenReturn(true);

        patientServlet.doPost(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void doPost_returnsForbiddenWhenRoleIsNull() throws Exception {
        when(req.getAttribute("role")).thenReturn(null);

        patientServlet.doPost(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doGet_returnsAllPatients() throws Exception {
        when(req.getAttribute("role")).thenReturn("Staff");
        when(req.getPathInfo()).thenReturn(null);

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(new Patient(), new Patient()));

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        patientServlet.doGet(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doGet_returnsPatientById() throws Exception {
        when(req.getAttribute("role")).thenReturn("Staff");
        when(req.getPathInfo()).thenReturn("/1");

        when(patientService.getPatientById(1)).thenReturn(new Patient(1, "John", "Galle", "071"));

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        patientServlet.doGet(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doPut_updatesPatientSuccessfully() throws Exception {
        when(req.getAttribute("role")).thenReturn("Admin");

        String json = "{\"patientId\":1,\"patientName\":\"John Doe\",\"address\":\"Galle\",\"contactNumber\":\"0712345678\"}";
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(patientService.updatePatient(any(Patient.class))).thenReturn(true);

        patientServlet.doPut(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDelete_deletesPatientSuccessfully() throws Exception {
        when(req.getAttribute("role")).thenReturn("Admin");
        when(req.getPathInfo()).thenReturn("/1");
        when(patientService.deletePatient(1)).thenReturn(true);

        patientServlet.doDelete(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void doDelete_returnsForbiddenWhenRoleIsStaff() throws Exception {
        when(req.getAttribute("role")).thenReturn("Staff");

        patientServlet.doDelete(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}