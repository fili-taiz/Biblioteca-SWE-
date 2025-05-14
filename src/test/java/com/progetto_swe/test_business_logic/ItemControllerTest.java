package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.ItemController;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItemControllerTest {
    Connection connection = ConnectionManager.getConnection();
    ItemController itemController = new ItemController();

    @BeforeEach
    public void setUp() throws SQLException{
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE book, item, hirer, reservation RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }


    @Test
    public void testSearchItem(){
        BookDAO bookDAO = new BookDAO();
        int book_code_1 = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1",  Library.LIBRARY_1.toString(), 5, true);
        int book_code_2 = bookDAO.addBook("Anatomia", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2",  Library.LIBRARY_1.toString(), 7, true);

        ArrayList<Item> expected_items = new ArrayList<>();

        expected_items.add(bookDAO.getBook(book_code_1));

        ArrayList<Item> notExpected_items = new ArrayList<>();
        notExpected_items.add(bookDAO.getBook(book_code_2));

        assertEquals(expected_items, itemController.searchItem("Fond", Category.CATEGORY_1.toString()));
        assertNotEquals(notExpected_items, itemController.searchItem("Fond", Category.CATEGORY_1.toString()));


    }

    /*testiamo solo per Book dato che la logica per Magazine e Thesis è la stessa. E' di interesse testare solo il caso in cui viene
    lanciata l'eccezione dal momento che, se l'esito è positivo, vengono semplicemente fatti degli inserimenti sul DB, i cui relativi
    metodi sono già stati testati nei test dell'ORM.
     */


    @Test
    public void testAddBook(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
        Token token = new Token(hirer);
        hirer.setToken(token);

        assertThrows(ActionDeniedException.class, () -> itemController.addBook("title", LocalDate.of(2004, 2, 13).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors", 5, true, token));
    }

    @Test
    public void testRemoveBook(){
        Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00000", Library.LIBRARY_1,null);
        Token admin_token = new Token(admin);
        admin.setToken(admin_token);
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);
        BookDAO bookDAO = new BookDAO();

        int book_code_1 = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1",  Library.LIBRARY_1.toString(), 5, true);
        int book_code_2 = bookDAO.addBook("Anatomia", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2",  Library.LIBRARY_2.toString(), 7, true);


        assertThrows(ActionDeniedException.class, () -> itemController.removeBook(book_code_1, hirer_token));
        assertThrows(ActionDeniedException.class, () -> itemController.removeBook(book_code_2, admin_token));

        ReservationDAO reservationDAO = new ReservationDAO();
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("usercode", "name", "surname", "email", "00001");
        reservationDAO.addReservation("usercode", book_code_1, Library.LIBRARY_1.toString());

        assertThrows(ActionDeniedException.class, () -> itemController.removeBook(book_code_1, admin_token));

    }

    @Test
    public void testUpdateBook(){
        Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
        Token token = new Token(hirer);
        hirer.setToken(token);

        assertThrows(ActionDeniedException.class, () -> itemController.updateBook(1,"title", LocalDate.of(2004, 2, 13).toString(), true, Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors", 5, token));

    }
}
