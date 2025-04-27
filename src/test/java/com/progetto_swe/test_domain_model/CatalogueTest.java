package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class CatalogueTest {

    @Test
    public void testConstructor(){
        ArrayList<Item> items = new ArrayList<>();
        Catalogue catalogue_1 = new Catalogue(items);

        assertEquals(items, catalogue_1.getItems());
    }

    @Test
    public void testContains(){
        Item book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Item book_2 = new Book(2, "title_2", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_2", 150, "authors_2");
        ArrayList<Item> items = new ArrayList<>();
        items.add(book_1);
        Catalogue catalogue = new Catalogue(items);

        assertEquals(1, catalogue.contains(book_1));
        assertEquals(-1, catalogue.contains(book_2));
    }

    @Test
    public void testGetItem(){
        Item book_1 = new Book(1, "title_1", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        ArrayList<Item> items = new ArrayList<>();
        items.add(book_1);
        Catalogue catalogue = new Catalogue(items);

        assertEquals(book_1, catalogue.getItem(1));
        assertNull(catalogue.getItem(2));
    }

    @Test
    public void testSearchItem(){
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
        Item book_1 = new Book(1, "analisi_matematica_1", LocalDate.of(2025, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 200, "authors");
        Item book_2 = new Book("analisi_matematica_2", LocalDate.of(2025, 3, 7), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_1", 200, "authors");
        Item book_3 = new Book("geometria_e_algebra_lineare", LocalDate.of(2025, 3, 8), Language.LANGUAGE_2, Category.CATEGORY_2, "link_3", "isbn_3", "publishing_house_1", 200, "authors");
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
        assertEquals(result, catalogue.searchItem("analisi_matematica", Category.CATEGORY_1));
        assertNotEquals(items, catalogue.searchItem("analisi_matematica", Category.CATEGORY_1));
    }

    @Test
    public void testAdvancedSearchItem(){
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
        Item book_1 = new Book(1, "analisi_matematica_1", LocalDate.of(2025, 3, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 200, "authors");
        Item book_2 = new Book("analisi_matematica_2", LocalDate.of(2025, 3, 7), Language.LANGUAGE_1, Category.CATEGORY_1, "link_2", "isbn_2", "publishing_house_1", 200, "authors");
        Item book_3 = new Book("geometria_e_algebra_lineare", LocalDate.of(2025, 3, 8), Language.LANGUAGE_2, Category.CATEGORY_2, "link_3", "isbn_3", "publishing_house_1", 200, "authors");

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
        assertEquals(result, catalogue.advancedSearchItem("analisi_matematica", Category.CATEGORY_1, Language.LANGUAGE_1, true, LocalDate.of(2021, 4, 3 ), LocalDate.of(2025, 4, 5)));
        assertNotEquals(items, catalogue.advancedSearchItem("analisi_matematica", Category.CATEGORY_1, Language.LANGUAGE_1, true, LocalDate.of(2021, 4, 3 ), LocalDate.of(2025, 4, 5)));
    }
}
