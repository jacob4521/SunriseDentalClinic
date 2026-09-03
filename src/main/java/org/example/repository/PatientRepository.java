package org.example.repository;

import org.example.model.Patient;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    public boolean addPatient(Patient patient) {
        if (patient == null
                || patient.getPatientName() == null || patient.getPatientName().isEmpty()
                || patient.getAddress() == null || patient.getAddress().isEmpty()
                || patient.getContactNumber() == null || patient.getContactNumber().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO patients (patient_name, address, contact_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("address"),
                        rs.getString("contact_number")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getInt("patient_id"),
                            rs.getString("patient_name"),
                            rs.getString("address"),
                            rs.getString("contact_number")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePatient(Patient patient) {
        if (patient == null
                || patient.getPatientName() == null || patient.getPatientName().isEmpty()
                || patient.getAddress() == null || patient.getAddress().isEmpty()
                || patient.getContactNumber() == null || patient.getContactNumber().isEmpty()) {
            return false;
        }

        String sql = "UPDATE patients SET patient_name = ?, address = ?, contact_number = ? WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());
            ps.setInt(4, patient.getPatientId());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
