package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.MagazineDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MagazineDAOTest {

    @BeforeEach
    public void setUp() throws Exception {
        Connection connection = ConnectionManager.getConnection();
        String query = "TRUNCATE TABLE Magazine, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @Test
    public void testGetMagazine() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        try{
            MagazineDAO magazineDAO = new MagazineDAO();
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            int magazine_code = magazineDAO.addMagazine("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "publishing house", 50);
            physicalCopiesDAO.addPhysicalCopies(magazine_code, Library.LIBRARY_1.toString(), 5, true);
            Magazine magazine_1 = new Magazine(magazine_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "publishing house");
            Magazine magazine_2 = new Magazine(magazine_code+1, "titolo2", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "publishing house");


            assertEquals(magazine_1, magazineDAO.getMagazine(magazine_code));
            assertNotEquals(magazine_2, magazineDAO.getMagazine(magazine_code));

        }finally {
            connection.close();
        }
    }

    @Test
    public void testAddMagazine() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        try{

            MagazineDAO magazineDAO = new MagazineDAO();
            Magazine magazine_1 = new Magazine(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "publishing house 1");

            assertEquals(magazine_1.getCode(), magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "publishing house 1", 50));
            assertNotEquals(3, magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  "publishing house 2", 55));

        }finally {
            connection.close();
        }

    }

    @Test
    public void testUpdateMagazine() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        try{
            MagazineDAO magazineDAO = new MagazineDAO();
            Magazine magazine_1 = new Magazine(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "publishing house 1");
            Magazine magazine_2 = new Magazine(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "publishing house 2");
            magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);

            assertTrue(magazineDAO.updateMagazine(magazine_1.getCode(), "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "publishing house 2"));
            assertFalse(magazineDAO.updateMagazine(magazine_2.getCode(), "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1"));


        }finally{
            connection.close();
        }
    }

    @Test
    public void testRemoveMagazine() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        try{
            MagazineDAO magazineDAO = new MagazineDAO();
            int magazine_code = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);
            Magazine magazine_2 = new Magazine(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "publishing house 2");
            assertTrue(magazineDAO.removeMagazine(magazine_code));
            assertFalse(magazineDAO.removeMagazine(magazine_2.getCode()));

        }finally {
            connection.close();
        }
    }




}
