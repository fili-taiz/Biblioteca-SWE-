package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class MagazineTest {

    @Test
    public void testConstructors(){
        Magazine magazine = new Magazine(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");

        assertEquals(1, magazine.getCode());
        assertEquals("title", magazine.getTitle());
        assertEquals(LocalDate.now(), magazine.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, magazine.getLanguage());
        assertEquals(Category.CATEGORY_1, magazine.getCategory());
        assertEquals("link", magazine.getLink());
        assertEquals("publishingHouse", magazine.getPublishingHouse());

        Magazine magazine1 = new Magazine("title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");
        assertEquals(-1, magazine1.getCode());
        assertEquals("title", magazine1.getTitle());
        assertEquals(LocalDate.now(), magazine1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, magazine1.getLanguage());
        assertEquals(Category.CATEGORY_1, magazine1.getCategory());
        assertEquals("link", magazine1.getLink());
        assertEquals("publishingHouse", magazine1.getPublishingHouse());

    }

    @Test
    public void testSameField(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Item magazine_1 = new Magazine(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");
        Item magazine_2 = new Magazine(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");

        assertFalse(magazine_1.sameField(item));
        assertTrue(magazine_1.sameField(magazine_2));
    }

    @Test
    public void testUpdateItem(){
        Magazine old_magazine = new Magazine(1, "old_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "old_link","old_publishingHouse");
        Item item = new Item(2, "new_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_3, "new_link");
        Item new_magazine = new Magazine(3, "new_title", LocalDate.now(), Language.LANGUAGE_2, Category.CATEGORY_2, "new_link", "new_publishingHouse");
        assertFalse(old_magazine.updateItem(item));
        assertTrue(old_magazine.updateItem(new_magazine));
    }

    @Test
    public void testContains(){
        Magazine magazine = new Magazine(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");
        assertTrue(magazine.contains("publ"));
        assertFalse(magazine.contains("universita"));
    }

    @Test
    public void testEquals(){
        Magazine magazine_1 = new Magazine(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");
        Magazine magazine_2 = magazine_1;
        assertTrue(magazine_1.equals(magazine_2));
        Magazine magazine = new Magazine(1, "title1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "publishingHouse");
        assertFalse(magazine.equals(magazine_2));
    }
}
