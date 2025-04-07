package com.progetto_swe.test.domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class HirerTest {

    @Test
    public void testConstructor(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null, null, null, null);

        assertEquals("usercode", hirer.getUserCode());
        assertEquals("name", hirer.getName());
        assertEquals("surname", hirer.getSurname());
        assertEquals("email", hirer.getEmail());
        assertEquals("00000", hirer.getTelephoneNumber());
        assertNull(hirer.getUserProfile());
        assertNull(hirer.getLendings());
        assertNull(hirer.getReservations());
        assertNull(hirer.getCatalogue());
        assertNull(hirer.getUnbannedDate());
    }

    @Test
    public void testHaveLending(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null, null, null, null);
        Item item_1 = new Item(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_2 = new Item(2, "title_2", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", 150);

        Lending lending_1 = new Lending(LocalDate.now(), hirer, item_1,Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.now(), hirer, item_2, Library.LIBRARY_2);

        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);

        hirer.setLendings(lendings);

        assertTrue(hirer.haveLending(lending_1));
        assertFalse(hirer.haveLending(lending_2));
    }

    @Test
    public void testHaveReservation(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null, null, null, null);
        Item item_1 = new Item(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_2 = new Item(2, "title_2", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", 150);

        Reservation reservation_1 = new Reservation(LocalDate.now(), hirer, item_1,Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.now(), hirer, item_2, Library.LIBRARY_2);

        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);

        hirer.setReservations(reservations);

        assertTrue(hirer.haveReservation(reservation_1));
        assertFalse(hirer.haveReservation(reservation_2));
    }

    @Test
    public void testContains(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "12345",
                null, null, null, null, null);

        assertTrue(hirer.contains("user"));
        assertTrue(hirer.contains("nam"));
        assertTrue(hirer.contains("surn"));
        assertTrue(hirer.contains("mai"));
        assertTrue(hirer.contains("12345"));
        assertFalse(hirer.contains("universita"));
    }
}
