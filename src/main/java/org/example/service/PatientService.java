package org.example.service;

import org.example.model.Patient;
import org.example.repository.PatientRepository;
import java.util.List;

public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public boolean addPatient(Patient patient) {
        if (patient == null
                || patient.getPatientName() == null || patient.getPatientName().isEmpty()
                || patient.getContactNumber() == null || patient.getContactNumber().isEmpty()) {
            return false;
        }

        return patientRepository.addPatient(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.getAllPatients();
    }

    public Patient getPatientById(int id) {
        return patientRepository.getPatientById(id);
    }

    public boolean updatePatient(Patient patient) {
        if (patient == null
                || patient.getPatientName() == null || patient.getPatientName().isEmpty()
                || patient.getContactNumber() == null || patient.getContactNumber().isEmpty()) {
            return false;
        }

        return patientRepository.updatePatient(patient);
    }

    public boolean deletePatient(int id) {
        return patientRepository.deletePatient(id);
    }
}
