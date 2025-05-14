package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LendingDAOTest {
    Connection connection = ConnectionManager.getConnection();
    BookDAO bookDAO = new BookDAO();
    LendingDAO lendingDAO = new LendingDAO();

    @BeforeEach
    public void setUp() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE lending, hirer, physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }


    @Test
    public void testGetLendingsByUserCode(){

        Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
        Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
        MagazineDAO magazineDAO = new MagazineDAO();
        int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1", Library.LIBRARY_1.toString(), 5, true);
        int magazine_code = magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50, Library.LIBRARY_2.toString(), 5, true);

        HirerDAO hirerDAO = new HirerDAO();
        Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
        Token token = new Token(hirer);
        hirer.setToken(token);
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

        lendingDAO.addLending("uc1", book_code, Library.LIBRARY_1.toString());
        lendingDAO.addLending("uc1", magazine_code, Library.LIBRARY_2.toString());

        ArrayList<Lending> expected_lendings = new ArrayList<>();

        Lending l1 = new Lending(LocalDate.now(), LocalDate.of(2025, 6, 14), hirer, book, Library.LIBRARY_1);
        Lending l2 = new Lending(LocalDate.now(), LocalDate.of(2025, 6, 14), hirer, magazine, Library.LIBRARY_2);

        expected_lendings.add(l1);
        expected_lendings.add(l2);


        assertEquals(expected_lendings.size(), lendingDAO.getLendingsByUserCode("uc1").size());
        assertTrue(lendingDAO.getLendingsByUserCode("uc1").containsAll(expected_lendings));

        assertTrue(lendingDAO.getLendingsByUserCode("uc2").isEmpty());

    }

    @Test
    public void testAddLending(){

        bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1", Library.LIBRARY_1.toString(), 5, true );
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString());

        assertThrows(IdAlreadyExistsException.class, () -> lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString()));


    }

    @Test
    public void testRemoveLending(){

        assertThrows(IdNotFoundException.class, () -> lendingDAO.removeLending("uc1", 1, Library.LIBRARY_1.toString()));

    }
}
