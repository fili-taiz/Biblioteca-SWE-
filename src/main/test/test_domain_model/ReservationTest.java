package test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ReservationTest {
    @Test
    public void testConstructor(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Reservation reservation = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);

        assertEquals(LocalDate.now(), reservation.getReservationDate());
        assertEquals(hirer, reservation.getHirer());
        assertEquals(item, reservation.getItem());
        assertEquals(Library.LIBRARY_1, reservation.getStoragePlace());

    }
}
