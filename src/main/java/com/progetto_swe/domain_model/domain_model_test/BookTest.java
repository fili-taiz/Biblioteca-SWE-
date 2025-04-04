package com.progetto_swe.domain_model.domain_model_test;

import com.progetto_swe.domain_model.Book;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void testConstructors(){
        Book book_1 = new Book(1, "title1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishingHouse1", 100, "authors1");

        assertEquals(1, book_1.getCode());
        assertEquals("title1", book_1.getTitle());
        assertEquals(LocalDate.now(), book_1.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, book_1.getLanguage());
        assertEquals(Category.CATEGORY_1, book_1.getCategory());
        assertEquals("link1", book_1.getLink());
        assertEquals("isbn1", book_1.getIsbn());
        assertEquals("publishingHouse1", book_1.getPublishingHouse());
        assertEquals(100, book_1.getNumberOfPages());
        assertEquals("authors1", book_1.getAuthors());

        Book book_2 = new Book("title2", LocalDate.now(), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", "isbn2", "publishingHouse2", 200, "authors2");

        assertEquals("title2", book_2.getTitle());
        assertEquals(LocalDate.now(), book_2.getPublicationDate());
        assertEquals(Language.LANGUAGE_2, book_2.getLanguage());
        assertEquals(Category.CATEGORY_2, book_2.getCategory());
        assertEquals("link2", book_2.getLink());
        assertEquals("isbn2", book_2.getIsbn());
        assertEquals("publishingHouse2", book_2.getPublishingHouse());
        assertEquals(200, book_2.getNumberOfPages());
        assertEquals("authors2", book_2.getAuthors());
    }

    @Test
    public void testUpdateItem(){
        Book old_book = new Book(1, "old_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "old_link", "old_isbn", "old_publishingHouse", 100, "authors");
        Item item = new Item(2, "new_title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_3, "new_link");
        Item new_book = new Book(3, "new_title", LocalDate.now(), Language.LANGUAGE_2, Category.CATEGORY_2, "new_link", "new_isbn", "new_publishingHouse", 100, "authors1");
        assertFalse(old_book.updateItem(item));
        assertTrue(old_book.updateItem(new_book));
    }

    @Test
    public void testSameField(){
        Item item = new Item(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link");
        Item book_1 = new Book(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishingHouse", 100, "authors");
        Item book_2 = new Book(2, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishingHouse", 100, "authors");

        assertFalse(book_1.sameField(item));
        assertTrue(book_1.sameField(book_2));
    }

    @Test
    public void testContains(){
        Book book = new Book(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "codicelibroisbn", "publishingHouse", 100, "authors");
        assertTrue(book.contains("ib"));
        assertTrue(book.contains("publ"));
        assertTrue(book.contains("thors"));
        assertFalse(book.contains("universita"));
    }

    @Test
    public void testEquals(){
        Book book_1 = new Book(1, "title", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "codicelibroisbn", "publishingHouse", 100, "authors");
        Book book_2 = book_1;
        assertTrue(book_1.equals(book_2));
        Book book = new Book(1, "title1", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "codicelibroisbn", "publishingHouse", 100, "authors");
        assertFalse(book.equals(book_2));
    }

}

