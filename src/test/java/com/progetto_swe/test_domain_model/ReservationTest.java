package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    @Test
    public void testConstructor(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Reservation reservation = new Reservation(LocalDate.of(2025,5,6), hirer, item, Library.LIBRARY_1);

        assertEquals(LocalDate.of(2025,5,6), reservation.getReservationDate());
        assertEquals(hirer, reservation.getHirer());
        assertEquals(item, reservation.getItem());
        assertEquals(Library.LIBRARY_1, reservation.getStoragePlace());

    }

    @Test
    public void testEquals(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item_1 = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item item_2 = new Magazine(2, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Reservation reservation_1 = new Reservation(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Reservation reservation_3 = new Reservation(LocalDate.of(2025,5,6), hirer, item_2, Library.LIBRARY_1);

        assertTrue(reservation_1.equals(reservation_2));
        assertFalse(reservation_1.equals(reservation_3));
        assertFalse(reservation_1.equals(null));
    }
}
