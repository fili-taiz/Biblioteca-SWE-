package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest {

    @BeforeEach
    public void setUp() throws Exception {
        Connection connection = ConnectionManager.getConnection();
        String query = "TRUNCATE TABLE Book, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @Test
    public void testGetBook() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        BookDAO bookDAO = new BookDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int book_code = bookDAO.addBook("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors");
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);
        Book book_1 = new Book(book_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing house", 200, "authors");
        Book book_2 = new Book(book_code+1, "titolo2", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing house", 200, "authors");


        assertEquals(book_1, bookDAO.getBook(book_code));
        assertNotEquals(book_2, bookDAO.getBook(book_code));

        connection.close();

    }

    @Test
    public void testAddBook() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        BookDAO bookDAO = new BookDAO();
        Book book_1 = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );

        assertEquals(book_1.getCode(), bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1" ));
        assertNotEquals(3, bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", 200, "authors2" ));
        connection.close();


    }

    @Test
    public void testUpdateBook() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        BookDAO bookDAO = new BookDAO();
        Book book_1 = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
        Book book_2 = new Book(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", "isbn2", "publishing house 2", 200, "authors2" );
        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");

        assertTrue(bookDAO.updateBook(book_1.getCode(), "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", "authors2"));
        assertFalse(bookDAO.updateBook(book_2.getCode(), "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", "authors1" ));

        connection.close();

    }

    @Test
    public void testRemoveBook() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        Book book_2 = new Book(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", "isbn2", "publishing house 2", 200, "authors2" );
        assertTrue(bookDAO.removeBook(book_code));
        assertFalse(bookDAO.removeBook(book_2.getCode()));

        connection.close();

    }




}
