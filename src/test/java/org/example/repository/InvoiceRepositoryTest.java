package org.example.repository;

import org.example.model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvoiceRepositoryTest {

    private InvoiceRepository invoiceRepository;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        invoiceRepository = new InvoiceRepository();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
    }

    @Test
    void addInvoice_returnsTrue_whenSuccessful() throws Exception {
        // Arrange
        Invoice invoice = new Invoice(0, 1, 2500.00, "2026-09-15", "Paid");

        try (MockedStatic<DatabaseConnection> mockedDb = Mockito.mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 Row affected

            // Act
            boolean result = invoiceRepository.addInvoice(invoice);

            // Assert
            assertTrue(result);
            verify(mockPreparedStatement, times(1)).setInt(1, invoice.getAppointmentId());
            verify(mockPreparedStatement, times(1)).setDouble(2, invoice.getTotalAmount());
            verify(mockPreparedStatement, times(1)).setString(3, invoice.getIssuedDate());
            verify(mockPreparedStatement, times(1)).setString(4, invoice.getPaymentStatus());
        }
    }
}