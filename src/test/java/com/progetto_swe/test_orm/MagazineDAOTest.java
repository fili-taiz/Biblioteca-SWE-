package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.MagazineDAO;
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

public class MagazineDAOTest {
    Connection connection = ConnectionManager.getConnection();

    @BeforeEach
    public void setUp() throws Exception {
        String query = "TRUNCATE TABLE Magazine, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetMagazine(){
        MagazineDAO magazineDAO = new MagazineDAO();
        int magazine_code = magazineDAO.addMagazine("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "publishing house", 50, Library.LIBRARY_1.toString(), 5, true);
        Magazine magazine_1 = new Magazine(magazine_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "publishing house");
        Magazine magazine_2 = new Magazine(magazine_code+1, "titolo2", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "publishing house");

        assertEquals(magazine_1, magazineDAO.getMagazine(magazine_code));
        assertNotEquals(magazine_2, magazineDAO.getMagazine(magazine_code));
        assertThrows(IdNotFoundException.class, () -> magazineDAO.getMagazine(5));

    }

    @Test
    public void testAddMagazine(){

        MagazineDAO magazineDAO = new MagazineDAO();
        Magazine magazine_1 = new Magazine(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "publishing house 1");

        assertEquals(magazine_1.getCode(), magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "publishing house 1", 50, Library.LIBRARY_1.toString(), 5, true));
        assertNotEquals(3, magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  "publishing house 2", 55, Library.LIBRARY_1.toString(), 5, true));

    }

    @Test
    public void testUpdateMagazine(){
        MagazineDAO magazineDAO = new MagazineDAO();

        Magazine magazine_2 = new Magazine(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "publishing house 2");
        magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50, Library.LIBRARY_1.toString(), 5, true);

        assertThrows(IdNotFoundException.class, () -> magazineDAO.updateMagazine(magazine_2.getCode(), "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1",  Library.LIBRARY_1.toString(), 5, true, 200));

    }

    @Test
    public void testRemoveMagazine(){

        MagazineDAO magazineDAO = new MagazineDAO();

        assertThrows(IdNotFoundException.class, () -> magazineDAO.removeMagazine(3));


    }

    @Test
    public void testGetAllMagazines(){
        MagazineDAO magazineDAO = new MagazineDAO();
        int code_1 = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 200,  Library.LIBRARY_1.toString(), 10,   false);
        int code_2 = magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "publishing house 2", 220, Library.LIBRARY_1.toString(), 10,  false);

        Magazine magazine_1 = magazineDAO.getMagazine(code_1);
        Magazine magazine_2 = magazineDAO.getMagazine(code_2);
        ArrayList<Magazine> expected = new ArrayList<>();
        expected.add(magazine_1);
        expected.add(magazine_2);

        ArrayList<Magazine> notExpected = new ArrayList<>();
        notExpected.add(magazine_1);

        assertEquals(expected, magazineDAO.getAllMagazines());
        assertNotEquals(notExpected, magazineDAO.getAllMagazines());
    }




}
