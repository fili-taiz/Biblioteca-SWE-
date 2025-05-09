package com.progetto_swe.test_domain_model;

import com.progetto_swe.domain_model.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testConstructors() {
        Book book_1 = new Book(1, "title_1", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Book book_2 = new Book("title_2", LocalDate.of(2023,5,6), Language.LANGUAGE_2, Category.CATEGORY_2, "link_2", "isbn_2", "publishing_house_2", 200, "authors_2");

        assertEquals(1, book_1.getCode());
        assertEquals("title_1", book_1.getTitle());
        assertEquals(LocalDate.of(2023,5,6), book_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, book_1.getLanguage());
        assertEquals(Category.CATEGORY_1, book_1.getCategory());
        assertEquals("link_1", book_1.getLink());
        assertEquals("isbn_1", book_1.getIsbn());
        assertEquals("publishing_house_1", book_1.getPublishingHouse());
        assertEquals(100, book_1.getNumberOfPages());
        assertEquals("authors_1", book_1.getAuthors());

        assertEquals(-1, book_2.getCode());
        assertEquals("title_2", book_2.getTitle());
        assertEquals(LocalDate.of(2023,5,6), book_2.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, book_2.getLanguage());
        assertEquals(Category.CATEGORY_2, book_2.getCategory());
        assertEquals("link_2", book_2.getLink());
        assertEquals("isbn_2", book_2.getIsbn());
        assertEquals("publishing_house_2", book_2.getPublishingHouse());
        assertEquals(200, book_2.getNumberOfPages());
        assertEquals("authors_2", book_2.getAuthors());

    }

    @Test
    public void testSameField(){
        Item magazine = new Magazine(1, "title_1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", 100, "publishing_house_1");
        Item book_1 = new Book(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Item book_2 = new Book(2, "title_1", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");
        Item book_3 = new Book(2, "title_3", LocalDate.of(2024, 5, 4), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", "isbn_1", "publishing_house_1", 100, "authors_1");

        assertTrue(book_1.sameField(book_2));
        assertFalse(book_2.sameField(magazine));
        assertFalse(book_3.sameField(book_2));
    }

    @Test
    public void testContains(){
        Book book = new Book(1, "title", LocalDate.of(2024, 5, 6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");

        assertTrue(book.contains("itl"));
        assertTrue(book.contains("024-05"));
        assertTrue(book.contains("ANGU"));
        assertTrue(book.contains("isb"));
        assertTrue(book.contains("publ"));
        assertTrue(book.contains("auth"));
        assertFalse(book.contains("abc"));

    }

    @Test
    public void testEquals(){
        Item magazine = new Magazine(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 100, "publishing_house");
        Item book_1 = new Book(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");
        Item book_2 = new Book(1, "title", LocalDate.of(2023,5,6), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing_house", 100, "authors");

        assertEquals(book_1, book_2);
        assertNotEquals(book_2, magazine);
        assertNotEquals(null, book_1);
    }
}
