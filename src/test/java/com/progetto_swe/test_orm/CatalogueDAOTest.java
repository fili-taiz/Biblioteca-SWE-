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
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogueDAOTest {

    @BeforeEach
    public void setUp() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testGetCatalogueFull() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
        Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
        Thesis thesis = new Thesis(3, "titolo3", LocalDate.of(2023,3,2), Language.LANGUAGE_1, Category.CATEGORY_1, "link3", 55, "author", "supervisors", "university");
        BookDAO bookDAO = new BookDAO();
        MagazineDAO magazineDAO = new MagazineDAO();
        ThesisDAO thesisDAO = new ThesisDAO();
        bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1" );
        magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50);
        thesisDAO.addThesis("titolo3", LocalDate.of(2023,3,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link3", 55, "author", "supervisors", "university");
        PhysicalCopies pc1 = new PhysicalCopies(10, true);
        PhysicalCopies pc2 = new PhysicalCopies(12, true);
        PhysicalCopies pc3 = new PhysicalCopies(1, false);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 10, true);
        physicalCopiesDAO.addPhysicalCopies(2, Library.LIBRARY_2.toString(), 12, true);
        physicalCopiesDAO.addPhysicalCopies(3, Library.LIBRARY_3.toString(), 1, false);

        ArrayList<Item> expectedItems = new ArrayList<>();

        HashMap <Library, PhysicalCopies> expectedmapbook = new HashMap<>();
        expectedmapbook.put(Library.LIBRARY_1, pc1);

        HashMap <Library, PhysicalCopies> expectedmapmagazine = new HashMap<>();
        expectedmapmagazine.put(Library.LIBRARY_2, pc2);

        HashMap <Library, PhysicalCopies> expectedmapthesis = new HashMap<>();
        expectedmapthesis.put(Library.LIBRARY_3, pc3);

        book.setPhysicalCopies(expectedmapbook);
        magazine.setPhysicalCopies(expectedmapmagazine);
        thesis.setPhysicalCopies(expectedmapthesis);

        expectedItems.add(book);
        expectedItems.add(magazine);
        expectedItems.add(thesis);

        Catalogue expectedCatalogue = new Catalogue(expectedItems);

        CatalogueDAO catalogueDAO = new CatalogueDAO();

        assertEquals(expectedCatalogue.getItems().size(), catalogueDAO.getCatalogue().getItems().size());

        assertTrue(catalogueDAO.getCatalogue().getItems().containsAll(expectedCatalogue.getItems()));

        connection.close();

    }

    @Test
    public void getCatalogueEmpty() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        CatalogueDAO catalogueDAO = new CatalogueDAO();
        assertNull(catalogueDAO.getCatalogue());
        connection.close();

    }
}
