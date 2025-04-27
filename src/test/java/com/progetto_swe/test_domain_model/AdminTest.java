package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AdminTest {

    @Test
    public void testConstructor() {
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);

        assertEquals("usercode", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(ucs, admin.getUserCredentials());
    }

    @Test
    public void testSearchItem() {
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);
        PhysicalCopies pcs_1 = new PhysicalCopies(0, true);
        PhysicalCopies pcs_2 = new PhysicalCopies(4, true);
        PhysicalCopies pcs_3 = new PhysicalCopies(5, true);
        PhysicalCopies pcs_4 = new PhysicalCopies(3, true);
        PhysicalCopies pcs_5 = new PhysicalCopies(4, true);
        HashMap<Library, PhysicalCopies> pcs = new HashMap<>();
        pcs.put(Library.LIBRARY_1, pcs_1);
        pcs.put(Library.LIBRARY_2, pcs_2);
        pcs.put(Library.LIBRARY_3, pcs_3);
        pcs.put(Library.LIBRARY_4, pcs_4);
        pcs.put(Library.LIBRARY_5, pcs_5);
        Item book_1 = new Book(1, "analisi_matematica_1", LocalDate.of(2022, 4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 200, "authors");
        Item book_2 = new Book("analisi_matematica_2", LocalDate.of(2022, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_1", 200, "authors");
        Item book_3 = new Book("geometria_e_algebra_lineare", LocalDate.of(2022, 5,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link_3", "isbn_3", "publishing_house_1", 200, "authors");
        book_1.setPhysicalCopies(pcs);
        book_2.setPhysicalCopies(pcs);
        book_3.setPhysicalCopies(pcs);
        ArrayList<Item> items = new ArrayList<>();
        items.add(book_1);
        items.add(book_2);
        items.add(book_3);
        Catalogue catalogue = new Catalogue(items);
        ArrayList<Item> result = new ArrayList<>();
        result.add(book_1);
        result.add(book_2);
        assertEquals(result, admin.searchItem(catalogue, "analisi_matematica", Category.CATEGORY_1));
        assertNotEquals(items, admin.searchItem(catalogue, "analisi_matematica", Category.CATEGORY_1));
    }

    @Test
    public void testAdvancedSearchItem(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);
        PhysicalCopies pcs_1 = new PhysicalCopies(0, true);
        PhysicalCopies pcs_2 = new PhysicalCopies(4, true);
        PhysicalCopies pcs_3 = new PhysicalCopies(5, true);
        PhysicalCopies pcs_4 = new PhysicalCopies(3, true);
        PhysicalCopies pcs_5 = new PhysicalCopies(4, true);
        HashMap<Library, PhysicalCopies> pcs = new HashMap<>();
        pcs.put(Library.LIBRARY_1, pcs_1);
        pcs.put(Library.LIBRARY_2, pcs_2);
        pcs.put(Library.LIBRARY_3, pcs_3);
        pcs.put(Library.LIBRARY_4, pcs_4);
        pcs.put(Library.LIBRARY_5, pcs_5);
        Item book_1 = new Book(1, "analisi_matematica_1", LocalDate.of(2022, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 200, "authors");
        Item book_2 = new Book("analisi_matematica_2", LocalDate.of(2022, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_1", 200, "authors");
        Item book_3 = new Book("geometria_e_algebra_lineare", LocalDate.of(2022, 5, 2), Language.LANGUAGE_2, Category.CATEGORY_2, "link_3", "isbn_3", "publishing_house_1", 200, "authors");
        book_1.setPhysicalCopies(pcs);
        book_2.setPhysicalCopies(pcs);
        book_3.setPhysicalCopies(pcs);
        ArrayList<Item> items = new ArrayList<>();
        items.add(book_1);
        items.add(book_2);
        items.add(book_3);
        Catalogue catalogue = new Catalogue(items);
        ArrayList<Item> result = new ArrayList<>();
        result.add(book_1);
        result.add(book_2);
        assertEquals(result, admin.advancedSearchItem(catalogue, "analisi_matematica", Category.CATEGORY_1, Language.LANGUAGE_1, true, LocalDate.of(2021, 4, 3 ), LocalDate.of(2025, 4, 5)));
        assertNotEquals(items, admin.advancedSearchItem(catalogue, "analisi_matematica", Category.CATEGORY_1, Language.LANGUAGE_1, true, LocalDate.of(2021, 4, 3 ), LocalDate.of(2025, 4, 5)));
    }

}

