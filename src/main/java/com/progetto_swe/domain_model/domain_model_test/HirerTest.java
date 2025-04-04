package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class HirerTest {

    @Test
    public void testConstructor(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", null, null, null, null, LocalDate.now());
        assertEquals("usercode", hirer.getUserCode());
        assertEquals("name", hirer.getName());
        assertEquals("surname", hirer.getSurname());
        assertEquals("email", hirer.getEmail());
        assertEquals("00000", hirer.getTelephoneNumber());
        assertNull(hirer.getUserProfile());
        assertNull(hirer.getLendings());
        assertNull(hirer.getReservations());
        assertNull(hirer.getCatalogue());
        assertEquals(LocalDate.now(), hirer.getUnbannedDate());
    }

    @Test
    public void testContains(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "03203", null, null, null, null, LocalDate.now());
        assertTrue(hirer.contains("serc"));
        assertTrue(hirer.contains("am"));
        assertTrue(hirer.contains("urn"));
        assertTrue(hirer.contains("email"));
        assertTrue(hirer.contains("emai"));
        assertTrue(hirer.contains("320"));
        assertFalse(hirer.contains("ingegneria"));

    }

    @Test
    public void testHaveReservation(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        ArrayList<Reservation> r = new ArrayList<>();
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", ucs, null, r, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Reservation reservation1 = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        Reservation reservation2 = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        r.add(reservation1);
        assertTrue(hirer.haveReservation(reservation1));
        assertFalse(hirer.haveReservation(reservation2));
    }

    @Test
    public void testHaveLending(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        ArrayList<Lending> l  = new ArrayList<>();
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", ucs, l, null, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Lending lending1 = new Lending(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        Lending lending2 = new Lending(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        l.add(lending1);
        item.setLendings(l);
        assertTrue(item.haveLending(lending1));
        assertFalse(item.haveLending(lending2));
    }
}
