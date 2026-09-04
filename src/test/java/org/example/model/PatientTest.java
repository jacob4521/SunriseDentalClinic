package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientTest {

    @Test
    void testPatientCreationAndGetters() {
        int expectedId = 1;
        String expectedName = "John Doe";
        String expectedAddress = "123 Galle Road, Colombo";
        String expectedContactNumber = "0771234567";

        Patient patient = new Patient(expectedId, expectedName, expectedAddress, expectedContactNumber);

        assertEquals(expectedId, patient.getPatientId());
        assertEquals(expectedName, patient.getPatientName());
        assertEquals(expectedAddress, patient.getAddress());
        assertEquals(expectedContactNumber, patient.getContactNumber());
    }
}