package org.example.service;

import org.example.model.Dentist;
import org.example.repository.DentistRepository;

import java.util.List;

public class DentistService {
    private final DentistRepository dentistRepository;

    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    public boolean addDentist(Dentist dentist) {
        if (dentist == null || dentist.getDentistName() == null || dentist.getDentistName().trim().isEmpty()) {
            return false;
        }
        return dentistRepository.addDentist(dentist);
    }

    public List<Dentist> getAllDentists() {
        return dentistRepository.getAllDentists();
    }

    public boolean deleteDentist(int id) {
        return dentistRepository.deleteDentist(id);
    }
}