package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.ThesisDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class ThesisDAOTest {
    Connection connection = ConnectionManager.getConnection();


    @BeforeEach
    public void setUp() throws SQLException {
        String query = "TRUNCATE TABLE Thesis, Item RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetThesis(){

        ThesisDAO thesisDAO = new ThesisDAO();
        int thesis_code = thesisDAO.addThesis("titolo", LocalDate.of(2023, 4, 5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", 50, "author", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false);

        Thesis thesis_1 = new Thesis(thesis_code, "titolo", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "author", "supervisors", "university");
        Thesis thesis_2 = new Thesis(thesis_code + 1, "titolo2", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "author2", "supervisors", "university");

        assertEquals(thesis_1, thesisDAO.getThesis(thesis_code));
        assertNotEquals(thesis_2, thesisDAO.getThesis(thesis_code));
        assertThrows(IdNotFoundException.class, () -> thesisDAO.getThesis(3));

    }

    @Test
    public void testAddThesis(){

        ThesisDAO thesisDAO = new ThesisDAO();
        Thesis thesis_1 = new Thesis(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "author1", "supervisors", "university");

        assertEquals(thesis_1.getCode(), thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  50, "author1", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false));
        assertNotEquals(3, thesisDAO.addThesis("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  55, "author2", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false));

    }

    @Test
    public void testUpdateThesis(){

        ThesisDAO thesisDAO = new ThesisDAO();
        thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false);

        assertThrows(IdNotFoundException.class, () -> thesisDAO.updateThesis(3, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "author2", "supervisors", "university", Library.LIBRARY_1.toString(), 2, false, 50));

    }

    @Test
    public void testRemoveThesis(){

        ThesisDAO thesisDAO = new ThesisDAO();

        thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false);

        assertThrows(IdNotFoundException.class, () -> thesisDAO.removeThesis(3));


    }

    @Test
    public void testGetAllThesis(){
        ThesisDAO thesisDAO = new ThesisDAO();
        int code_1 = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university", Library.LIBRARY_1.toString(), 1, false);
        int code_2 = thesisDAO.addThesis("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", 55, "author2", "supervisors_2", "university_2", Library.LIBRARY_1.toString(), 1, false);

        Thesis thesis_1 = thesisDAO.getThesis(code_1);
        Thesis thesis_2 = thesisDAO.getThesis(code_2);
        ArrayList<Thesis> expected = new ArrayList<>();
        expected.add(thesis_1);
        expected.add(thesis_2);

        ArrayList<Thesis> notExpected = new ArrayList<>();
        notExpected.add(thesis_1);

        assertEquals(expected, thesisDAO.getAllThesis());
        assertNotEquals(notExpected, thesisDAO.getAllThesis());

    }


}
