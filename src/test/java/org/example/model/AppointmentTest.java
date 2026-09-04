package org.example.model;

import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.sql.Time;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppointmentTest {

    @Test
    void testAppointmentCreationAndGetters() {
        // Expected values
        int expectedAppointmentId = 1;
        int expectedPatientId = 10;
        int expectedDentistId = 2;
        int expectedTreatmentId = 3;
        Date expectedDate = Date.valueOf("2026-09-15");
        Time expectedTime = Time.valueOf("10:30:00");

        // Object creation (මෙතනින් Error එකක් එනවා, මොකද Class එක තාම නැති නිසා)
        Appointment appointment = new Appointment(expectedAppointmentId, expectedPatientId, expectedDentistId, expectedTreatmentId, expectedDate, expectedTime);

        // Assertions
        assertEquals(expectedAppointmentId, appointment.getAppointmentId());
        assertEquals(expectedPatientId, appointment.getPatientId());
        assertEquals(expectedDentistId, appointment.getDentistId());
        assertEquals(expectedTreatmentId, appointment.getTreatmentId());
        assertEquals(expectedDate, appointment.getAppointmentDate());
        assertEquals(expectedTime, appointment.getAppointmentTime());
    }
}