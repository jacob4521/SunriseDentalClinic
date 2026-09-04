package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Invoice;
import org.example.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServletTest {

    private InvoiceServlet invoiceServlet;
    private InvoiceService mockInvoiceService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        mockInvoiceService = mock(InvoiceService.class);
        invoiceServlet = new InvoiceServlet(mockInvoiceService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void doPost_createsInvoice_whenDataIsValid() throws Exception {
        // Arrange - JSON data from Postman
        String jsonPayload = "{\"appointmentId\": 1, \"totalAmount\": 2500.00, \"issuedDate\": \"2026-09-15\", \"paymentStatus\": \"Paid\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonPayload)));
        when(mockInvoiceService.addInvoice(any(Invoice.class))).thenReturn(true);

        // Act
        invoiceServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED); // 201
        assertTrue(responseWriter.toString().contains("Invoice created successfully"));
    }
}