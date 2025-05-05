package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.ThesisDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ThesisDAOTest {


    @BeforeEach
    public void setUp() throws Exception {
        Connection connection = ConnectionManager.getConnection();
        String query = "TRUNCATE TABLE Thesis, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @Test
    public void testGetThesis() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);

        try {
            ThesisDAO thesisDAO = new ThesisDAO();
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            int thesis_code = thesisDAO.addThesis("titolo", LocalDate.of(2023, 4, 5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", 50, "author", "supervisors", "university");
            physicalCopiesDAO.addPhysicalCopies(thesis_code, Library.LIBRARY_1.toString(), 1, false);
            Thesis thesis_1 = new Thesis(thesis_code, "titolo", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "author", "supervisors", "university");
            Thesis thesis_2 = new Thesis(thesis_code + 1, "titolo2", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "author2", "supervisors", "university");


            assertEquals(thesis_1, thesisDAO.getThesis(thesis_code));
            assertNotEquals(thesis_2, thesisDAO.getThesis(thesis_code));
            connection.rollback();

        }finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Test
    public void testAddThesis() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{

            ThesisDAO thesisDAO = new ThesisDAO();
            Thesis thesis_1 = new Thesis(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "author1", "supervisors", "university");

            assertEquals(thesis_1.getCode(), thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  50, "author1", "supervisors", "university"));
            assertNotEquals(3, thesisDAO.addThesis("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  55, "author2", "supervisors", "university"));
            connection.rollback();

        }finally {
            connection.setAutoCommit(true);
            connection.close();
        }

    }

    @Test
    public void testUpdateThesis() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{

            ThesisDAO thesisDAO = new ThesisDAO();
            Thesis thesis_1 = new Thesis(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "author1", "supervisors", "university");
            Thesis thesis_2 = new Thesis(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "author2", "supervisors", "university");
            thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university");

            assertTrue(thesisDAO.updateThesis(thesis_1.getCode(), "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "author2", "supervisors", "university"));
            assertFalse(thesisDAO.updateThesis(thesis_2.getCode(), "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "author2", "supervisors", "university"));

            connection.rollback();


        }finally{
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    @Test
    public void testRemoveThesis() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try{
            ThesisDAO thesisDAO = new ThesisDAO();
            int thesis_code = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  50, "author1", "supervisors", "university");
            Thesis thesis_2 = new Thesis(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "author2", "supervisors", "university");
            assertTrue(thesisDAO.removeThesis(thesis_code));
            assertFalse(thesisDAO.removeThesis(thesis_2.getCode()));



            connection.rollback();


        }finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }




}
