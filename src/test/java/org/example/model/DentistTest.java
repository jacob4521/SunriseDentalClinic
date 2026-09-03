package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DentistTest {

    @Test
    void testDentistCreationAndGetters() {
        Dentist dentist = new Dentist(1, "Dr. Kamal Perera");

        assertEquals(1, dentist.getDentistId());
        assertEquals("Dr. Kamal Perera", dentist.getDentistName());
    }
}