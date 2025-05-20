package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.ConstraintViolationException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class MagazineDAOTest {
    Connection connection = ConnectionManager.getInstance().getConnection();
    MagazineDAO magazineDAO = new MagazineDAO();

    @BeforeEach
    public void setUp() throws SQLException {
        String query = "TRUNCATE TABLE Magazine, Item, Physical_copies, Lending, Hirer RESTART IDENTITY CASCADE;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetMagazine(){
        int magazine_code = magazineDAO.addMagazine("titolo", LocalDate.of(2023,4,5).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link", "publishing house", 50);
        Magazine magazine_1 = new Magazine(magazine_code, "titolo", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 50, "publishing house");
        Magazine magazine_2 = new Magazine(magazine_code+1, "titolo2", LocalDate.of(2023,4,5), Language.LANGUAGE_1, Category.CATEGORY_1, "link", 55, "publishing house");

        assertEquals(magazine_1, magazineDAO.getMagazine(magazine_code));
        assertNotEquals(magazine_2, magazineDAO.getMagazine(magazine_code));
        assertThrows(IdNotFoundException.class, () -> magazineDAO.getMagazine(5));

    }

    @Test
    public void testAddMagazine(){

        Magazine magazine_1 = new Magazine(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", 50, "publishing house 1");

        int itemCode = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);
        magazine_1.setCode(itemCode);
        Magazine copy_of_magazine_1 = magazineDAO.getMagazine(magazine_1.getCode());
        assertEquals(magazine_1, copy_of_magazine_1);
        assertNotEquals(3, magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2",  "publishing house 2", 55));

    }

    @Test
    public void testUpdateMagazine(){

        Magazine magazine_2 = new Magazine(2, "titolo2", LocalDate.of(2023,4,2), Language.LANGUAGE_2, Category.CATEGORY_2, "link2", 55, "publishing house 2");
        int magazine_code = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);
        //item code non presente nel database
        assertThrows(IdNotFoundException.class, () -> magazineDAO.updateMagazine(99, "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 200));

        //update con successo
        Magazine magazine_inserted = magazineDAO.getMagazine(magazine_code);
        magazine_2.setCode(magazine_code);
        assertNotEquals(magazine_2, magazine_inserted);
        magazineDAO.updateMagazine(magazine_code, "titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_2.toString(), Category.CATEGORY_2.toString(), "link2", "publishing house 2", 200);
        magazine_inserted = magazineDAO.getMagazine(magazine_code);
        assertEquals(magazine_2, magazine_inserted);

        //update di un book tramite updateMagazine
        BookDAO bookDAO = new BookDAO();
        int bookCode = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        assertThrows(IdNotFoundException.class, ()-> magazineDAO.updateMagazine(bookCode, "titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 200));
    }

    @Test
    public void testRemoveMagazine(){

        int magazine_code =  magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name", "surname", "email", "00000");
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.addPhysicalCopies(magazine_code, Library.LIBRARY_1.toString(), 2, true);
        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.addLending("uc1", magazine_code, Library.LIBRARY_1.toString());

        //id non presente
        assertThrows(IdNotFoundException.class, () -> magazineDAO.removeMagazine(3));
        //cancellazione di magazine con ancora un prestito non restituito
        assertThrows(ConstraintViolationException.class, () -> magazineDAO.removeMagazine(magazine_code));


        //cancellazione di un book tramite removeMagazine
        BookDAO bookDAO = new BookDAO();
        int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
        assertThrows(IdNotFoundException.class, () -> magazineDAO.removeMagazine(book_code));

        //cancellazione con successo
        int magazine_code2 = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 50);
        magazineDAO.getMagazine(magazine_code2);
        magazineDAO.removeMagazine(magazine_code2);
        assertThrows(IdNotFoundException.class, () -> magazineDAO.getMagazine(magazine_code2));
    }

    @Test
    public void testGetAllMagazines(){

        int code_1 = magazineDAO.addMagazine("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "publishing house 1", 200);
        int code_2 = magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "publishing house 2", 220);

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
