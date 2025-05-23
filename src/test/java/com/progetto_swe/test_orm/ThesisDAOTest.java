package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.ConstraintViolationException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.*;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class ThesisDAOTest {
    static Connection connection;
    ThesisDAO thesisDAO = new ThesisDAO();

    @BeforeAll
    public static void setUpBeforeClass(){
        connection = ConnectionManager.getInstance().getConnection();
    }

    @AfterAll
    public static void tearDown() throws SQLException{
        connection.close();
    }


    @BeforeEach
    public void setUp() throws SQLException {
        String query = "TRUNCATE TABLE Thesis, Item, Physical_copies, Lending, Hirer RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }


    @Test
    public void testGetThesis(){

        int thesis_code = thesisDAO.addThesis("titolo", LocalDate.of(2023, 4, 5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", 50, "author", "supervisors", "university");

        Thesis thesis_1 = new Thesis(thesis_code, "titolo", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "author", "supervisors", "university");
        Thesis thesis_2 = new Thesis(thesis_code + 1, "titolo2", LocalDate.of(2023, 4, 5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "author2", "supervisors", "university");

        assertEquals(thesis_1, thesisDAO.getThesis(thesis_code));
        assertNotEquals(thesis_2, thesisDAO.getThesis(thesis_code));
        assertThrows(IdNotFoundException.class, () -> thesisDAO.getThesis(3));

    }

    @Test
    public void testAddThesis(){

        Thesis thesis_1 = new Thesis(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "author1", "supervisors", "university");

        int thesis_code = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university");
        thesis_1.setCode(thesis_code);
        Thesis copy_of_thesis_1 = thesisDAO.getThesis(thesis_code);
        assertEquals(thesis_1, copy_of_thesis_1);
        assertNotEquals(3, thesisDAO.addThesis("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  55, "author2", "supervisors", "university"));

    }

    @Test
    public void testUpdateThesis(){

        Thesis thesis_2 = new Thesis(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 50, "author2", "supervisors", "university");
        int thesis_code = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university");
        //item code non presente nel database
        assertThrows(IdNotFoundException.class, () -> thesisDAO.updateThesis(99, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "author2", "supervisors", "university", 50));

        //update con successo
        Thesis thesis_inserted = thesisDAO.getThesis(thesis_code);
        thesis_2.setCode(thesis_code);
        assertNotEquals(thesis_2, thesis_inserted);
        thesisDAO.updateThesis(thesis_code, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "author2", "supervisors", "university", 50);
        thesis_inserted = thesisDAO.getThesis(thesis_code);
        assertEquals(thesis_2, thesis_inserted);

        //update di un book tramite updateThesis
        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        assertThrows(IdNotFoundException.class, ()-> thesisDAO.updateThesis(book_code, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "author2", "supervisors", "university", 50));
    }

    @Test
    public void testRemoveThesis(){

        int thesis_code = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university");
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name", "surname", "email", "00000");
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(thesis_code, Library.LIBRARY_1.toString(), 2, true);
        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.addLending("uc1", thesis_code, Library.LIBRARY_1.toString());

        //is non presente
        assertThrows(IdNotFoundException.class, () -> thesisDAO.removeThesis(3));
        //cancellazione di un thesis con ancora un prestito non restituito
        assertThrows(ConstraintViolationException.class, () -> thesisDAO.removeThesis(thesis_code));

        //cancellazione di un book tramite removeThesis
        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "isbn2", "publishing house 2", 200, "authors2");
        assertThrows(IdNotFoundException.class, () -> thesisDAO.removeThesis(book_code));


        //cancellazione con successo
        int thesis_code2 = thesisDAO.addThesis("titolo3", LocalDate.of(2023,4,3).toString(), Language.LANGUAGE_3.toString(), Category.CATEGORY_3.toString(), "link3", 50, "author3", "supervisors", "university");
        thesisDAO.getThesis(thesis_code2);
        thesisDAO.removeThesis(thesis_code2);
        assertThrows(IdNotFoundException.class, () -> thesisDAO.getThesis(thesis_code2));
    }

    @Test
    public void testGetAllThesis(){
        int code_1 = thesisDAO.addThesis("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", 50, "author1", "supervisors", "university");
        int code_2 = thesisDAO.addThesis("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", 55, "author2", "supervisors_2", "university_2");

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
