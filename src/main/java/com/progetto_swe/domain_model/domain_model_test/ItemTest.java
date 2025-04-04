package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void testConstructor(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");

        assertEquals(1, item.getCode());
        assertEquals("title", item.getTitle());
        assertEquals(LocalDate.now(), item.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, item.getLanguage());
        assertEquals(Category.CATEGORY_1, item.getCategory());
        assertEquals("link", item.getLink());
    }

    @Test
    public void testContains(){
        Item item = new Item(1, "title", LocalDate.of(2025, 4, 2),   Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        assertTrue(item.contains("itl"));
        assertTrue(item.contains("02"));
        assertTrue(item.contains("angu"));
        assertFalse(item.contains("05"));
    }

    @Test
    public void testSameField(){
        Item item1 = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Item item2 = item1;
        Item item3 = new Item(1, "title3", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link3");

        assertTrue(item1.equals(item2));
        assertFalse(item1.equals(item3));
    }

    @Test
    public void testHaveReservation(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", ucs, null, null, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Reservation reservation1 = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        Reservation reservation2 = new Reservation(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        ArrayList<Reservation> r = item.getReservations();
        r.add(reservation1);
        item.setReservations(r);
        assertTrue(item.haveReservation(reservation1));
        assertFalse(item.haveReservation(reservation2));
    }

    @Test
    public void testHaveLending(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000", ucs, null, null, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Lending lending1 = new Lending(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        Lending lending2 = new Lending(LocalDate.now(), hirer, item, Library.LIBRARY_1);
        ArrayList<Lending> l = item.getLendings();
        l.add(lending1);
        item.setLendings(l);
        assertTrue(item.haveLending(lending1));
        assertFalse(item.haveLending(lending2));
    }

    @Test
    public void testEquals(){
        Item item_1 = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Item item_2 = item_1;
        assertTrue(item_1.equals(item_2));
        Item item = new Item(1, "title1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        assertFalse(item.equals(item_2));
    }

    @Test
    public void testAvailableCopies(){
        UserCredentials ucs1 = new UserCredentials("usercode1", "hashed_password1");
        UserCredentials ucs2 = new UserCredentials("usercode2", "hashed_password2");
        UserCredentials ucs3 = new UserCredentials("usercode3", "hashed_password3");
        Hirer hirer1 = new Hirer("usercode1", "name1", "surname1", "email1", "00001", ucs1, null, null, null, null);
        Hirer hirer2 = new Hirer("usercode2", "name2", "surname2", "email2", "00002", ucs2, null, null, null, null);
        Hirer hirer3 = new Hirer("usercode3", "name3", "surname3", "email3", "00003", ucs3, null, null, null, null);
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Lending l1 = new Lending(LocalDate.now(), hirer1, item, Library.LIBRARY_1);
        Lending l2 = new Lending(LocalDate.now(), hirer2, item, Library.LIBRARY_1);
        Reservation r1 = new Reservation(LocalDate.now(), hirer3, item, Library.LIBRARY_1);

        ArrayList<Lending> nl = item.getLendings();
        nl.add(l1);
        nl.add(l2);
        ArrayList<Reservation> nr = item.getReservations();
        nr.add(r1);

        item.setLendings(nl);
        item.setReservations(nr);

        HashMap<Library, PhysicalCopies> hm_pcs = item.getPhysicalCopies();
        PhysicalCopies pcs = new PhysicalCopies(10, true);
        hm_pcs.put(Library.LIBRARY_1, pcs);
        item.setPhysicalCopies(hm_pcs);

        assertEquals(7, item.getNumberOfAvailableCopiesInLibrary(Library.LIBRARY_1));
        assertNotEquals(8, item.getNumberOfAvailableCopiesInLibrary(Library.LIBRARY_1));

    }

    @Test
    public void testIsBorrowable(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        HashMap<Library, PhysicalCopies> hm_pcs = item.getPhysicalCopies();
        PhysicalCopies pcs = new PhysicalCopies(10, false);
        hm_pcs.put(Library.LIBRARY_1, pcs);
        item.setPhysicalCopies(hm_pcs);
        assertFalse(item.isBorrowable(Library.LIBRARY_1));
        assertFalse(item.isBorrowable(Library.LIBRARY_2));

    }

    @Test
    public void testUpdateItem(){
        Item item_1 = new Item(1, "title1", LocalDate.of(2025, 4, 2), Language.LANGUAGE_1, Category.CATEGORY_1, "link1");
        Item item_2 = new Item(2, "title2", LocalDate.of(2025, 4, 3), Language.LANGUAGE_2, Category.CATEGORY_2, "link2");

        item_1.updateItem(item_2);

        assertEquals("title2", item_1.getTitle());
        assertEquals(LocalDate.of(2025, 4, 3), item_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, item_1.getLanguage());
        assertEquals(Category.CATEGORY_2, item_1.getCategory());
        assertEquals("link2", item_1.getLink());
    }


}
