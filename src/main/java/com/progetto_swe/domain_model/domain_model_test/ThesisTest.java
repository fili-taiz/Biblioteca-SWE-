package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.*;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ThesisTest {

    @Test
    public void testConstructors(){
        Thesis thesis = new Thesis(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");

        assertEquals(1, thesis.getCode());
        assertEquals("title", thesis.getTitle());
        assertEquals(LocalDate.now(), thesis.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, thesis.getLanguage());
        assertEquals(Category.CATEGORY_1, thesis.getCategory());
        assertEquals("author", thesis.getAuthor());
        assertEquals("supervisors", thesis.getSupervisors());
        assertEquals("university", thesis.getUniversity());

        Thesis thesis1 = new Thesis("title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");

        assertEquals(-1, thesis1.getCode());
        assertEquals("title", thesis1.getTitle());
        assertEquals(LocalDate.now(), thesis1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, thesis1.getLanguage());
        assertEquals(Category.CATEGORY_1, thesis1.getCategory());
        assertEquals("author", thesis1.getAuthor());
        assertEquals("supervisors", thesis1.getSupervisors());
        assertEquals("university", thesis1.getUniversity());


    }

    @Test
    public void testSameField(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Item thesis_1 = new Thesis(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");
        Item thesis_2 = new Thesis(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");

        assertFalse(thesis_1.sameField(item));
        assertTrue(thesis_1.sameField(thesis_2));
    }

    @Test
    public void testUpdateItem(){
        Thesis old_thesis = new Thesis(1, "old_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "old_link","old_author", "old_supervisors", "old_university");
        Item item = new Item(2, "new_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_3, "new_link");
        Item new_thesis = new Thesis(3, "new_title", LocalDate.now(), Language.LANGUAGE_2, Category.CATEGORY_2, "new_link", "new_author", "new_supervisors", "new_university");
        assertFalse(old_thesis.updateItem(item));
        assertTrue(old_thesis.updateItem(new_thesis));
    }

    @Test
    public void testContains(){
        Thesis thesis = new Thesis(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");
        assertTrue(thesis.contains("uthor"));
        assertFalse(thesis.contains("universita"));
    }

    @Test
    public void testEquals(){
        Thesis thesis_1 = new Thesis(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");
        Thesis thesis_2 = thesis_1;
        assertTrue(thesis_1.equals(thesis_2));
        Thesis thesis = new Thesis(1, "title1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "author", "supervisors", "university");
        assertFalse(thesis.equals(thesis_1));
    }
}
