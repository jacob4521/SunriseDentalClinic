package org.example.model;

public class Dentist {
    private int dentistId;
    private String dentistName;

    // No-argument constructor
    public Dentist() {
    }

    // All-arguments constructor
    public Dentist(int dentistId, String dentistName) {
        this.dentistId = dentistId;
        this.dentistName = dentistName;
    }

    // Getters and Setters
    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }
}