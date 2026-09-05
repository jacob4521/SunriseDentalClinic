package org.example.repository;

import org.example.model.Dentist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DentistRepository {
    private final Connection connection;

    public DentistRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean addDentist(Dentist dentist) {
        String query = "INSERT INTO dentists (dentist_name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dentist.getDentistName());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Dentist> getAllDentists() {
        List<Dentist> dentists = new ArrayList<>();
        String query = "SELECT dentist_id, dentist_name FROM dentists";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Dentist dentist = new Dentist(
                        resultSet.getInt("dentist_id"),
                        resultSet.getString("dentist_name")
                );
                dentists.add(dentist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dentists;
    }

    public boolean deleteDentist(int id) {
        String query = "DELETE FROM dentists WHERE dentist_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}