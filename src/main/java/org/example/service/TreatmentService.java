package org.example.service;

import org.example.model.Treatment;
import org.example.repository.TreatmentRepository;

import java.util.List;

public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public boolean addTreatment(Treatment treatment) {
        // Data Validation Logic
        if (treatment.getTreatmentType() == null || treatment.getTreatmentType().trim().isEmpty()) {
            return false;
        }
        if (treatment.getPrice() < 0) {
            return false;
        }

        // Pass to repository if validation is successful
        return treatmentRepository.addTreatment(treatment);
    }

    public List<Treatment> getAllTreatments() {
        // Retrieve data from repository
        return treatmentRepository.getAllTreatments();
    }

    public boolean deleteTreatment(int id) {
        return treatmentRepository.deleteTreatment(id);
    }
}