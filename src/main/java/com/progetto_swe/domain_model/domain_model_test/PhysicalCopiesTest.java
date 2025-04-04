package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.PhysicalCopies;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhysicalCopiesTest {

    @Test
    public void testConstructor(){
        PhysicalCopies pc1 = new PhysicalCopies(100, true);
        PhysicalCopies pc2 = new PhysicalCopies(200, false);

        assertEquals(100, pc1.getNumberOfPhysicalCopies());
        assertTrue(pc1.isBorrowable());

        assertEquals(200, pc2.getNumberOfPhysicalCopies());
        assertFalse(pc2.isBorrowable());

    }
}
