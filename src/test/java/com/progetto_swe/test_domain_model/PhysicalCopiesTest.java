package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.PhysicalCopies;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicalCopiesTest {

    @Test
    public void testConstructor(){
        PhysicalCopies pc = new PhysicalCopies(100, true);

        assertEquals(100, pc.getNumberOfPhysicalCopies());
        assertTrue(pc.isBorrowable());
    }

    @Test
    public void testEquals(){
        PhysicalCopies pc_1 = new PhysicalCopies(100, true);
        PhysicalCopies pc_2 = new PhysicalCopies(100, true);
        PhysicalCopies pc_3 = new PhysicalCopies(104, true);
        PhysicalCopies pc_4 = new PhysicalCopies(100, false);

        assertEquals(pc_1, pc_2);
        assertNotEquals(pc_1, pc_3);
        assertNotEquals(pc_1, pc_4);
        assertNotEquals(null, pc_1);
    }
}
