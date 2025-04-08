package test_domain_model;

import com.progetto_swe.domain_model.PhysicalCopies;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhysicalCopiesTest {

    @Test
    public void testConstructor(){
        PhysicalCopies pc = new PhysicalCopies(100, true);

        assertEquals(100, pc.getNumberOfPhysicalCopies());
        assertTrue(pc.isBorrowable());
    }
}
