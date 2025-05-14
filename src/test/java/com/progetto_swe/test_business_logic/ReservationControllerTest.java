package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.ReservationController;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationControllerTest {
    ReservationController reservationController = new ReservationController();
    Connection connection = ConnectionManager.getConnection();
    HirerDAO hirerDAO = new HirerDAO();
    BookDAO bookDAO = new BookDAO();
    AdminDAO adminDAO = new AdminDAO();

    @BeforeEach
    public void setUp() throws Exception {
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE book, item, hirer, reservation, admin RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    private int setup(){
        hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
        int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1",  Library.LIBRARY_1.toString(), 5, true);
        return book_code;
    }

    @Test
    public void removeReservation_Fail1(){
        Book book = bookDAO.getBook(setup());
        Hirer hirer = hirerDAO.getHirer("usercode");
        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);

        assertThrows(ActionDeniedException.class, () -> reservationController.removeReservation(hirer, book, Library.LIBRARY_1.toString(), hirer_token));
    }

    @Test
    public void removeReservation_Fail2(){
        Book book = bookDAO.getBook(setup());
        Hirer hirer = hirerDAO.getHirer("usercode");
        adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
        Admin admin = adminDAO.getAdmin("usercode1");
        Token token = new Token(admin);
        admin.setToken(token);

        assertThrows(ActionDeniedException.class, () -> reservationController.removeReservation(hirer, book, Library.LIBRARY_2.toString(), token));
    }

    @Test
    public void reserveItem_Fail1(){
        Book book = bookDAO.getBook(setup());
        adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
        Admin admin = adminDAO.getAdmin("usercode1");
        Token token = new Token(admin);
        admin.setToken(token);

        assertThrows(ActionDeniedException.class, () -> reservationController.reserveItem(hirerDAO.getHirer("usercode"), book, Library.LIBRARY_1.toString(), token));
    }

    @Test
    public void reserveItem_Fail2(){
        Book book = bookDAO.getBook(setup());
        Hirer hirer = hirerDAO.getHirer("usercode");
        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);
        hirer.setUnbannedDate(LocalDate.now());

        assertThrows(ActionDeniedException.class, () -> reservationController.reserveItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token));
    }

    @Test
    public void testConfirmReservationWithdraw_Fail1(){
        Book book = bookDAO.getBook(setup());
        Hirer hirer = hirerDAO.getHirer("usercode");
        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);

        assertThrows(ActionDeniedException.class, () -> reservationController.removeReservation(hirer, book, Library.LIBRARY_2.toString(), hirer_token));
    }

    @Test
    public void testConfirmReservationWithdraw_Fail2(){
        Book book = bookDAO.getBook(setup());
        adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
        Admin admin = adminDAO.getAdmin("usercode1");
        Token token = new Token(admin);
        admin.setToken(token);

        assertThrows(ActionDeniedException.class, () -> reservationController.reserveItem(hirerDAO.getHirer("usercode"), book, Library.LIBRARY_2.toString(), token));
    }

}
