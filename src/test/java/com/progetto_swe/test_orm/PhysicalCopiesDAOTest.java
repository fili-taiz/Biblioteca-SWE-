package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicalCopiesDAOTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testAddPhysicalCopies() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        BookDAO bookDAO = new BookDAO();

        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        assertTrue(physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 10, true));
        assertFalse(physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true));

        connection.close();

    }

    @Test
    public void testRemovePhysicalCopies() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        BookDAO bookDAO = new BookDAO();

        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1" );
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 10, true);
        assertTrue(physicalCopiesDAO.removePhysicalCopies(1, Library.LIBRARY_1.toString()));
        assertFalse(physicalCopiesDAO.removePhysicalCopies(1, Library.LIBRARY_2.toString()));

        connection.close();

    }

    @Test
    public void testUpdatePhysicalCopies() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        BookDAO bookDAO = new BookDAO();

        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 10, true);
        assertTrue(physicalCopiesDAO.updatePhysicalCopies(1, Library.LIBRARY_1.toString(), 14, true));
        assertFalse(physicalCopiesDAO.updatePhysicalCopies(2, Library.LIBRARY_2.toString(), 14, true));

        connection.close();

    }

    @Test
    public void testGetPhysicalCopies() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        Book b1 = new Book(1, "titolo1", LocalDate.of(2023, 4, 1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1");
        PhysicalCopies pc1 = new PhysicalCopies(10, true);
        PhysicalCopies pc2 = new PhysicalCopies(12, true);
        HashMap<Library, PhysicalCopies> pcExpected = new HashMap<>();
        pcExpected.put(Library.LIBRARY_1, pc1);
        pcExpected.put(Library.LIBRARY_2, pc2);
        b1.setPhysicalCopies(pcExpected);
        BookDAO bookDAO = new BookDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 10, true);
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_2.toString(), 12, true);

        assertEquals(pcExpected, physicalCopiesDAO.getPhysicalCopies(1));
        assertEquals(Collections.emptyMap(),physicalCopiesDAO.getPhysicalCopies(2));

        connection.close();

    }
}
