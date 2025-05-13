package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import com.progetto_swe.orm.ThesisDAO;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest {
    Connection connection = ConnectionManager.getConnection();

    @BeforeEach
    public void setUp() throws Exception {
        String query = "TRUNCATE TABLE Book, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetBook(){

        BookDAO bookDAO = new BookDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int book_code = bookDAO.addBook("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors", Library.LIBRARY_1.toString(), 5, true);
        Book book = new Book(book_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing house", 200, "authors");


        assertEquals(book, bookDAO.getBook(book_code));
        assertThrows(IdNotFoundException.class, () -> bookDAO.getBook(book_code+1));


    }

    @Test
    public void testAddBook(){
        BookDAO bookDAO = new BookDAO();
        Book book_1 = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );

        assertEquals(book_1.getCode(), bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1", Library.LIBRARY_1.toString(), 5, true ));
        assertNotEquals(3, bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", 200, "authors2", Library.LIBRARY_1.toString(), 5, true ));



    }

    @Test
    public void testUpdateBook(){

        BookDAO bookDAO = new BookDAO();

        Book book_2 = new Book(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", "isbn2", "publishing house 2", 200, "authors2" );
        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1",  Library.LIBRARY_1.toString(), 5, true);

        assertThrows(IdNotFoundException.class, () -> bookDAO.updateBook(book_2.getCode(), "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", "authors2", Library.LIBRARY_1.toString(), 5, true, 200));


    }

    @Test
    public void testRemoveBook(){

        BookDAO bookDAO = new BookDAO();

        assertThrows(IdNotFoundException.class, () -> bookDAO.removeBook(3));


    }

    @Test
    public void testGetAllBooks(){
        BookDAO bookDAO = new BookDAO();
        int code_1 = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1", Library.LIBRARY_1.toString(), 10, false);
        int code_2 = bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2", Library.LIBRARY_1.toString(), 10, false);

        Book book_1 = bookDAO.getBook(code_1);
        Book book_2 = bookDAO.getBook(code_2);
        ArrayList<Book> expected = new ArrayList<>();
        expected.add(book_1);
        expected.add(book_2);

        ArrayList<Book> notExpected = new ArrayList<>();
        notExpected.add(book_1);

        assertEquals(expected, bookDAO.getAllBooks());
        assertNotEquals(notExpected, bookDAO.getAllBooks());
    }




}
