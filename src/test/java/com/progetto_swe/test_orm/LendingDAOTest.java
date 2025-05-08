package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LendingDAOTest {
    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE lending, hirer, physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testGetLendings() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
        Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
        BookDAO bookDAO = new BookDAO();
        MagazineDAO magazineDAO = new MagazineDAO();
        bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50);

        HirerDAO hirerDAO = new HirerDAO();
        Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);
        physicalCopiesDAO.addPhysicalCopies(2, Library.LIBRARY_2.toString(), 6, true);


        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString());
        lendingDAO.addLending("uc1", 2, Library.LIBRARY_2.toString());


        ArrayList<Lending> expected_lendings = new ArrayList<>();

        Lending l1 = new Lending(LocalDate.now(), hirer, book, Library.LIBRARY_1);
        Lending l2 = new Lending(LocalDate.now(), hirer, magazine, Library.LIBRARY_2);

        expected_lendings.add(l1);
        expected_lendings.add(l2);

        ListOfLendings expected_list_of_lendings = new ListOfLendings(expected_lendings);

        assertEquals(expected_list_of_lendings.getLendings().size(), lendingDAO.getLendings_().getLendings().size());
        assertTrue(lendingDAO.getLendings_().getLendings().containsAll(expected_lendings));

        Lending l3 = new Lending(LocalDate.now(), hirer, magazine, Library.LIBRARY_3);

        assertFalse(lendingDAO.getLendings_().getLendings().contains(l3));

        connection.close();

    }

    @Test
    public void testAddLending() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        LendingDAO lendingDAO = new LendingDAO();
        BookDAO bookDAO = new BookDAO();
        bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);

        assertTrue(lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString()));
        assertFalse(lendingDAO.addLending("uc1", 1, Library.LIBRARY_2.toString()));

        connection.close();

    }

    @Test
    public void testRemoveLending() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        LendingDAO lendingDAO = new LendingDAO();
        BookDAO bookDAO = new BookDAO();
        bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);

        lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString());
        assertTrue(lendingDAO.removeLending("uc1", 1, Library.LIBRARY_1.toString()));
        assertFalse(lendingDAO.removeLending("uc1", 1, Library.LIBRARY_2.toString()));
        assertFalse(lendingDAO.removeLending("uc2", 1, Library.LIBRARY_2.toString()));

        connection.close();

    }
}
