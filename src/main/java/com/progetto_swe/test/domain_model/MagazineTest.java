package com.progetto_swe.test.domain_model;

import com.progetto_swe.domain_model.Magazine;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class MagazineTest {

    @Test
    public void testConstructors() {
        Magazine magazine_1 = new Magazine(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 30, "publishing_house_1");
        Magazine magazine_2 = new Magazine("title_2", LocalDate.now(), Language.LANGUAGE_2, Category.CATEGORY_2, "link_2", 40, "publishing_house_2");

        assertEquals(1, magazine_1.getCode());
        assertEquals("title_1", magazine_1.getTitle());
        assertEquals(LocalDate.now(), magazine_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, magazine_1.getLanguage());
        assertEquals(Category.CATEGORY_1, magazine_1.getCategory());
        assertEquals("link_1", magazine_1.getLink());
        assertEquals(30, magazine_1.getNumberOfPages());
        assertEquals("publishing_house_1", magazine_1.getPublishingHouse());

        assertEquals(-1, magazine_2.getCode());
        assertEquals("title_2", magazine_2.getTitle());
        assertEquals(LocalDate.now(), magazine_2.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, magazine_2.getLanguage());
        assertEquals(Category.CATEGORY_2, magazine_2.getCategory());
        assertEquals("link_2", magazine_2.getLink());
        assertEquals(40, magazine_2.getNumberOfPages());
        assertEquals("publishing_house_2", magazine_2.getPublishingHouse());

    }

    @Test
    public void testUpdateItem(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);
        Item old_magazine = new Magazine(2, "old_title", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "old_link", 100, "old_publishing_house");
        Item new_magazine = new Magazine(3, "new_title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_2, Category.CATEGORY_2, "new_link", 200, "new_publishing_house");

        assertTrue(old_magazine.updateItem(new_magazine));
        assertFalse(old_magazine.updateItem(item));
    }

    @Test
    public void testSameField(){
        Item item = new Item(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100);
        Item magazine_1 = new Magazine(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");
        Item magazine_2 = new Magazine(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");
        Item magazine_3 = new Magazine(2, "title_3", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");

        assertTrue(magazine_1.sameField(magazine_2));
        assertFalse(magazine_2.sameField(item));
        assertFalse(magazine_3.sameField(magazine_2));
    }

    @Test
    public void testContains(){
        Magazine magazine = new Magazine(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");

        assertTrue(magazine.contains("itl"));
        assertTrue(magazine.contains("024-05"));
        assertTrue(magazine.contains("ANGU"));
        assertTrue(magazine.contains("publ"));
        assertFalse(magazine.contains("abc"));

    }

    @Test
    public void testEquals(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100);
        Item magazine_1 = new Magazine(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item magazine_2 = new Magazine(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");

        assertTrue(magazine_1.equals(magazine_2));
        assertFalse(magazine_2.equals(item));
    }
}
