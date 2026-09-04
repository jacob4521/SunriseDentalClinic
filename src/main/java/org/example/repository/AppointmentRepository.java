package org.example.repository;

import org.example.model.Appointment;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentRepository {

    public boolean addAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, dentist_id, treatment_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDentistId());
            ps.setInt(3, appointment.getTreatmentId());
            ps.setDate(4, appointment.getAppointmentDate());
            ps.setTime(5, appointment.getAppointmentTime());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            // Overlapping appointment එකක් ආවොත් Database Trigger එක හරහා මෙතනට SQLException එකක් එනවා
            System.err.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("dentist_id"),
                        rs.getInt("treatment_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

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

    public Map<String, Object> getAppointmentWithPatientDetails(int appointmentId) {
        Map<String, Object> details = null;
        // SQL JOIN Query එක මඟින් tables දෙකේම දත්ත එකවර ලබා ගැනීම
        String query = "SELECT a.appointment_id, a.appointment_date, a.appointment_time, " +
                "p.patient_name, p.contact_number, p.address " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "WHERE a.appointment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                details = new HashMap<>();
                details.put("appointmentId", rs.getInt("appointment_id"));
                details.put("appointmentDate", rs.getString("appointment_date"));
                details.put("appointmentTime", rs.getString("appointment_time"));
                details.put("patientName", rs.getString("patient_name"));
                details.put("contactNumber", rs.getString("contact_number"));
                details.put("address", rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (Join): " + e.getMessage());
        }
        return details;
    }
}