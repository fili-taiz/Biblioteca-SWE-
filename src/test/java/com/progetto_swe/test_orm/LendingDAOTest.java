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
        connection.setAutoCommit(false);
        try{
            Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
            Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
            BookDAO bookDAO = new BookDAO();
            MagazineDAO magazineDAO = new MagazineDAO();
            bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
            magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50);

            HirerDAO hirerDAO = new HirerDAO();
            Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
            hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);
            physicalCopiesDAO.addPhysicalCopies(2, Library.LIBRARY_2.toString(), 6, true);


            LendingDAO lendingDAO = new LendingDAO();
            assertTrue(lendingDAO.addLending("uc1", 1, Library.LIBRARY_1.toString()));
            assertTrue(lendingDAO.addLending("uc1", 2, Library.LIBRARY_2.toString()));


            ArrayList<Lending> expected_lendings = new ArrayList<>();

            Lending l1 = new Lending(LocalDate.of(2025,4,3), hirer, book, Library.LIBRARY_1);
            Lending l2 = new Lending(LocalDate.of(2025,4,4), hirer, magazine, Library.LIBRARY_2);

            expected_lendings.add(l1);
            expected_lendings.add(l2);

            ListOfLendings expected_list_of_lendings = new ListOfLendings(expected_lendings);

            ArrayList<Lending> unexpected_lendings = new ArrayList<>();
            unexpected_lendings.add(l1);

            ListOfLendings unexpected_list_of_lendings = new ListOfLendings(unexpected_lendings);

            assertEquals(expected_list_of_lendings.getLendings().size(), lendingDAO.getLendings_().getLendings().size());


            assertTrue(lendingDAO.getLendings_().getLendings().containsAll(expected_lendings));

            assertNotEquals(unexpected_list_of_lendings.getLendings().size(), lendingDAO.getLendings_().getLendings().size());
            assertFalse(lendingDAO.getLendings_().getLendings().containsAll(unexpected_lendings));






        }finally{
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}
