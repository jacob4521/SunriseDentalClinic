package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreatmentTest {

    @Test
    void testTreatmentCreationAndGetters() {
        Treatment treatment = new Treatment(1, "Teeth Whitening", 5000.00);

        assertEquals(1, treatment.getTreatmentId());
        assertEquals("Teeth Whitening", treatment.getTreatmentType()); // Changed to getTreatmentType
        assertEquals(5000.00, treatment.getPrice());
    }
}