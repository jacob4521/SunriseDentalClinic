package org.example.repository;

import org.example.model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepository {

    private final Connection connection;

    public TreatmentRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean addTreatment(Treatment treatment) {
        String sql = "INSERT INTO treatments (treatment_type, price) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, treatment.getTreatmentType());
            stmt.setDouble(2, treatment.getPrice());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Treatment> getAllTreatments() {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM treatments";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Treatment treatment = new Treatment(
                        rs.getInt("treatment_id"),
                        rs.getString("treatment_type"),
                        rs.getDouble("price")
                );
                treatments.add(treatment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatments;
    }

    public boolean deleteTreatment(int id) {
        String sql = "DELETE FROM treatments WHERE treatment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}