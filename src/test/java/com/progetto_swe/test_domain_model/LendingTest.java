package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LendingTest {

    @Test
    public void testConstructor(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Lending lending = new Lending(LocalDate.of(2025,5,6), hirer, item, Library.LIBRARY_1);

        assertEquals(LocalDate.of(2025,5,6), lending.getLendingDate());
        assertEquals(hirer, lending.getHirer());
        assertEquals(item, lending.getItem());
        assertEquals(Library.LIBRARY_1, lending.getStoragePlace());
    }

    @Test
    public void testEquals(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00000",
                null, null);
        Item item_1 = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item item_2 = new Magazine(2, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Lending lending_1 = new Lending(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Lending lending_2 = new Lending(LocalDate.of(2025,5,6), hirer, item_1, Library.LIBRARY_1);
        Lending lending_3 = new Lending(LocalDate.of(2025,5,6), hirer, item_2, Library.LIBRARY_1);

        assertTrue(lending_1.equals(lending_2));
        assertFalse(lending_1.equals(lending_3));
        assertFalse(lending_1.equals(null));
    }
}
