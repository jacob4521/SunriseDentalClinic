package org.example.model;

public class Treatment {
    private int treatmentId;
    private String treatmentType; // Changed from treatmentName
    private double price;

    public Treatment() {
    }

    public Treatment(int treatmentId, String treatmentType, double price) {
        this.treatmentId = treatmentId;
        this.treatmentType = treatmentType;
        this.price = price;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}