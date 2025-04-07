package com.progetto_swe.test.domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ItemTest {
    @Test
    public void testConstructor() {
        Item item= new Item(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 50);

        assertEquals(1, item.getCode());
        assertEquals("title_1", item.getTitle());
        assertEquals(LocalDate.now(), item.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, item.getLanguage());
        assertEquals(Category.CATEGORY_1, item.getCategory());
        assertEquals("link_1", item.getLink());
        assertEquals(50, item.getNumberOfPages());
    }

    @Test
    public void testUpdateItem(){
        Item old_item = new Item(2, "old_title", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "old_link", 100);
        Item new_item = new Item(3, "new_title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_2, Category.CATEGORY_2, "new_link", 200);

        assertTrue(old_item.updateItem(new_item));
    }

    @Test
    public void testSameField(){
        Item item_1 = new Item(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_2 = new Item(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item item_3 = new Item(2, "title_3", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);

        assertTrue(item_1.sameField(item_2));
        assertFalse(item_3.sameField(item_2));
    }

    @Test
    public void testContains(){
        Item item = new Item(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);

        assertTrue(item.contains("itl"));
        assertTrue(item.contains("024-05"));
        assertTrue(item.contains("ANGU"));
        assertFalse(item.contains("abc"));

    }

    @Test
    public void testEquals(){
        Item item_1 = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);
        Item item_2 = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);
        Item item_3 = new Item(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);
        Item thesis = new Thesis(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "author", "supervisors", "university");

        assertTrue(item_1.equals(item_2));
        assertFalse(item_1.equals(thesis));
        assertFalse(item_1.equals(item_3));
        assertFalse(item_1.equals(null));
    }

    @Test
    public void testHaveLending(){
        Item item = new Item(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);

        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "00001", ucs_1, null, null, null, null);
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "00002", ucs_2, null, null, null, null);
        Lending lending_1 = new Lending(LocalDate.now(), hirer_1, item, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.now(), hirer_2, item, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        item.setLendings(lendings);

        assertTrue(item.haveLending(lending_1));
        assertFalse(item.haveLending(lending_2));


    }

    @Test
    public void testHaveReservation(){
        Item item = new Item(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);

        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "00001", ucs_1, null, null, null, null);
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "00002", ucs_2, null, null, null, null);
        Reservation reservation_1 = new Reservation(LocalDate.now(), hirer_1, item, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.now(), hirer_2, item, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        item.setReservations(reservations);

        assertTrue(item.haveReservation(reservation_1));
        assertFalse(item.haveReservation(reservation_2));

    }

    @Test
    public void testIsBorrowable(){
        PhysicalCopies pc1 = new PhysicalCopies(5, true);
        PhysicalCopies pc2 = new PhysicalCopies(6, true);
        PhysicalCopies pc3 = new PhysicalCopies(7, true);
        PhysicalCopies pc4 = new PhysicalCopies(6, true);
        PhysicalCopies pc5 = new PhysicalCopies(5, false);
        HashMap<Library, PhysicalCopies> pcs = new HashMap<>();
        pcs.put(Library.LIBRARY_1, pc1);
        pcs.put(Library.LIBRARY_2, pc2);
        pcs.put(Library.LIBRARY_3, pc3);
        pcs.put(Library.LIBRARY_4, pc4);
        pcs.put(Library.LIBRARY_5, pc5);

        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);

        item.setPhysicalCopies(pcs);

        assertTrue(item.isBorrowable(Library.LIBRARY_1));
        assertFalse(item.isBorrowable(Library.LIBRARY_5));

    }

    @Test
    public void testGetNumberOfAvailableCopiesInLibrary(){ //questo metodo va a testare tutti e 3 i metodi della classe item che mi permettono di determinare il numero di copie attualmente disponibili di un certo item all'interno di una biblioteca
        Item item = new Item(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);

        UserCredentials ucs_1 = new UserCredentials("usercode_1", "hashed_password_1");
        UserCredentials ucs_2 = new UserCredentials("usercode_2", "hashed_password_2");
        Hirer hirer_1 = new Hirer("usercode_1", "name_1", "surname_1", "email_1", "00001", ucs_1, null, null, null, null);
        Hirer hirer_2 = new Hirer("usercode_2", "name_2", "surname_2", "email_2", "00002", ucs_2, null, null, null, null);
        Lending lending_1 = new Lending(LocalDate.now(), hirer_1, item, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.now(), hirer_2, item, Library.LIBRARY_1);
        ArrayList<Lending> lendings = new ArrayList<>();
        lendings.add(lending_1);
        lendings.add(lending_2);
        item.setLendings(lendings);
        Reservation reservation_1 = new Reservation(LocalDate.now(), hirer_1, item, Library.LIBRARY_1);
        Reservation reservation_2 = new Reservation(LocalDate.now(), hirer_2, item, Library.LIBRARY_1);
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation_1);
        reservations.add(reservation_2);
        item.setReservations(reservations);
        PhysicalCopies pc = new PhysicalCopies(7, true);
        HashMap<Library, PhysicalCopies> pcs = new HashMap<>();
        pcs.put(Library.LIBRARY_1, pc);
        item.setPhysicalCopies(pcs);

        assertEquals(3, item.getNumberOfAvailableCopiesInLibrary(Library.LIBRARY_1));
        assertEquals(-1, item.getNumberOfAvailableCopiesInLibrary(Library.LIBRARY_2));

    }
}
