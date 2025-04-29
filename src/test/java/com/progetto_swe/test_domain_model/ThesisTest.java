package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ThesisTest {
    @Test
    public void testConstructors() {
        Thesis thesis_1 = new Thesis(1, "title_1", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 50, "author_1", "supervisors_1", "university");
        Thesis thesis_2 = new Thesis("title_2", LocalDate.of(2023,5,6), Language.LANGUAGE_2, Category.CATEGORY_2, "link_2", 60, "author_2", "supervisors_2", "university");

        assertEquals(1, thesis_1.getCode());
        assertEquals("title_1", thesis_1.getTitle());
        assertEquals(LocalDate.of(2023,5,6), thesis_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, thesis_1.getLanguage());
        assertEquals(Category.CATEGORY_1, thesis_1.getCategory());
        assertEquals("link_1", thesis_1.getLink());
        assertEquals(50, thesis_1.getNumberOfPages());
        assertEquals("author_1", thesis_1.getAuthor());
        assertEquals("supervisors_1", thesis_1.getSupervisors());
        assertEquals("university", thesis_1.getUniversity());

        assertEquals(-1, thesis_2.getCode());
        assertEquals("title_2", thesis_2.getTitle());
        assertEquals(LocalDate.of(2023,5,6), thesis_2.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, thesis_2.getLanguage());
        assertEquals(Category.CATEGORY_2, thesis_2.getCategory());
        assertEquals("link_2", thesis_2.getLink());
        assertEquals(60, thesis_2.getNumberOfPages());
        assertEquals("author_2", thesis_2.getAuthor());
        assertEquals("supervisors_2", thesis_2.getSupervisors());
        assertEquals("university", thesis_2.getUniversity());

    }

    @Test
    public void testSameField(){
        Item book = new Book(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Item thesis_1 = new Thesis(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "author_1", "supervisors_1", "university_1");
        Item thesis_2 = new Thesis(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "author_1", "supervisors_1", "university_1");
        Item thesis_3 = new Thesis(2, "title_3", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "author_1", "supervisors_1", "university_1");

        assertTrue(thesis_1.sameField(thesis_2));
        assertFalse(thesis_2.sameField(book));
        assertFalse(thesis_3.sameField(thesis_2));
    }

    @Test
    public void testContains(){
        Thesis thesis = new Thesis(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "author", "supervisors", "university");

        assertTrue(thesis.contains("itl"));
        assertTrue(thesis.contains("024-05"));
        assertTrue(thesis.contains("ANGU"));
        assertTrue(thesis.contains("auth"));
        assertTrue(thesis.contains("sup"));
        assertTrue(thesis.contains("uni"));
        assertFalse(thesis.contains("abc"));

    }

    @Test
    public void testEquals(){
        Item book = new Book(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");
        Item thesis_1 = new Thesis(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "author", "supervisors", "university");
        Item thesis_2 = new Thesis(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "author", "supervisors", "university");

        assertTrue(thesis_1.equals(thesis_2));
        assertFalse(thesis_2.equals(book));
    }
}
