package org.example.repository;

import org.example.model.Invoice;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceRepository {

    public boolean addInvoice(Invoice invoice) {
        String query = "INSERT INTO invoices (appointment_id, total_amount, issued_date, payment_status) VALUES (?, ?, ?, ?)";
        boolean isSaved = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, invoice.getAppointmentId());
            ps.setDouble(2, invoice.getTotalAmount());
            ps.setString(3, invoice.getIssuedDate());
            ps.setString(4, invoice.getPaymentStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isSaved = true;
            }

        } catch (SQLException e) {
            System.err.println("Database Error (Invoice): " + e.getMessage());
        }

        return isSaved;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = null;
        String query = "SELECT * FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, invoiceId);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                invoice = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("appointment_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("issued_date"),
                        rs.getString("payment_status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database Error (Get Invoice): " + e.getMessage());
        }
        return invoice;
    }
}