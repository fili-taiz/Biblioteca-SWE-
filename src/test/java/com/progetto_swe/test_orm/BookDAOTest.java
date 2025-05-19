package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.ConstraintViolationException;
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
    BookDAO bookDAO = new BookDAO();

    @BeforeEach
    public void setUp() throws SQLException {
        String query = "TRUNCATE TABLE Book, Item, Physical_copies, Lending, Hirer RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetBook(){

        int book_code = bookDAO.addBook("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors");
        Book book = new Book(book_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", "isbn", "publishing house", 200, "authors");


        assertEquals(book, bookDAO.getBook(book_code));
        assertThrows(IdNotFoundException.class, () -> bookDAO.getBook(book_code+1));


    }

    @Test
    public void testAddBook(){
        Book book_1 = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );

        int itemCode = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        book_1.setCode(itemCode);
        Book copy_of_book_1 = bookDAO.getBook(book_1.getCode());
        assertEquals(book_1, copy_of_book_1);
        assertNotEquals(3, bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", 200, "authors2"));



    }

    @Test
    public void testUpdateBook(){

        Book book_2 = new Book(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", "isbn2", "publishing house 2", 200, "authors2" );
        int itemCode = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        //item code non presente nel database
        assertThrows(IdNotFoundException.class, () -> bookDAO.updateBook(99, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", "authors2", 200));

        //update con successo
        Book book_inserted = bookDAO.getBook(itemCode);
        book_2.setCode(itemCode);
        assertNotEquals(book_2, book_inserted);
        bookDAO.updateBook(itemCode, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", "authors2", 200);
        book_inserted = bookDAO.getBook(itemCode);
        assertEquals(book_2, book_inserted);

        //update di un magazine tramite updateBook
        MagazineDAO magazineDAO = new MagazineDAO();
        int magazineCode = magazineDAO.addMagazine("titolo3", "2003-03-03", "lingua1", "categoria", "link", "publishinghouse", 200);
        assertThrows(IdNotFoundException.class, ()-> bookDAO.updateBook(magazineCode, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", "authors2", 200));


    }

    @Test
    public void testRemoveBook(){

        int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name", "surname", "email", "00000");
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 2, true);
        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.addLending("uc1", book_code, Library.LIBRARY_1.toString());

        //id non presente
        assertThrows(IdNotFoundException.class, () -> bookDAO.removeBook(3));
        //cancellazione di book con ancora un prestito non restituito
        assertThrows(ConstraintViolationException.class, () -> bookDAO.removeBook(book_code));

        //cancellazione di un magazine tramite removeBook
        MagazineDAO magazineDAO = new MagazineDAO();
        int magazineCode = magazineDAO.addMagazine("titolo3", "2003-03-03", "lingua1", "categoria", "link", "publishinghouse", 200);
        assertThrows(IdNotFoundException.class, () -> bookDAO.removeBook(magazineCode));

        //cancellazione con successo
        int book_code2 = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        bookDAO.getBook(book_code2);
        bookDAO.removeBook(book_code2);
        assertThrows(IdNotFoundException.class, () -> bookDAO.getBook(book_code2));
    }

    @Test
    public void testGetAllBooks(){
        int code_1 = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        int code_2 = bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2");

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
