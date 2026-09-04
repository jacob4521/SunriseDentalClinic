package org.example.service;

import org.example.model.Invoice;
import org.example.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private InvoiceRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = mock(InvoiceRepository.class);
        // Error එකක් පෙන්වයි, මොකද InvoiceService එක තාම නැති නිසා
        invoiceService = new InvoiceService(mockRepository);
    }

    @Test
    void addInvoice_returnsTrue_whenRepositorySucceeds() {
        // Arrange
        Invoice invoice = new Invoice(0, 1, 2500.00, "2026-09-15", "Paid");
        when(mockRepository.addInvoice(invoice)).thenReturn(true);

        // Act
        boolean result = invoiceService.addInvoice(invoice);

        // Assert
        assertTrue(result);
        verify(mockRepository, times(1)).addInvoice(invoice);
    }
}