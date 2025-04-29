package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class MagazineTest {

    @Test
    public void testConstructors() {
        Magazine magazine_1 = new Magazine(1, "title_1", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 30, "publishing_house_1");
        Magazine magazine_2 = new Magazine("title_2", LocalDate.of(2023,5,6), Language.LANGUAGE_2, Category.CATEGORY_2, "link_2", 40, "publishing_house_2");

        assertEquals(1, magazine_1.getCode());
        assertEquals("title_1", magazine_1.getTitle());
        assertEquals(LocalDate.of(2023,5,6), magazine_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, magazine_1.getLanguage());
        assertEquals(Category.CATEGORY_1, magazine_1.getCategory());
        assertEquals("link_1", magazine_1.getLink());
        assertEquals(30, magazine_1.getNumberOfPages());
        assertEquals("publishing_house_1", magazine_1.getPublishingHouse());

        assertEquals(-1, magazine_2.getCode());
        assertEquals("title_2", magazine_2.getTitle());
        assertEquals(LocalDate.of(2023,5,6), magazine_2.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, magazine_2.getLanguage());
        assertEquals(Category.CATEGORY_2, magazine_2.getCategory());
        assertEquals("link_2", magazine_2.getLink());
        assertEquals(40, magazine_2.getNumberOfPages());
        assertEquals("publishing_house_2", magazine_2.getPublishingHouse());

    }

    @Test
    public void testSameField(){
        Item book = new Book(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1",100, "authors_1");
        Item magazine_1 = new Magazine(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");
        Item magazine_2 = new Magazine(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");
        Item magazine_3 = new Magazine(2, "title_3", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");

        assertTrue(magazine_1.sameField(magazine_2));
        assertFalse(magazine_2.sameField(book));
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
        Item thesis = new Thesis(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "author_1", "supervisors_1", "university");
        Item magazine_1 = new Magazine(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item magazine_2 = new Magazine(1, "title", LocalDate.of(2023, 5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");

        assertTrue(magazine_1.equals(magazine_2));
        assertFalse(magazine_2.equals(thesis));
    }
}
