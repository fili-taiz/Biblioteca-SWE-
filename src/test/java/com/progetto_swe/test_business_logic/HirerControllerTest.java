package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.business_logic.*;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.WaitingListDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HirerControllerTest {
   Connection connection_library_db = ConnectionManager.getConnection();
    Connection connection_university_db;
    HirerDAO hirerDAO = new HirerDAO();
    BookDAO bookDAO = new BookDAO();
    WaitingListDAO waitingListDAO = new WaitingListDAO();
    HirerController hirerController = new HirerController();


    @BeforeEach
    public void setUp() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        PreparedStatement preparedStatement = connection_university_db.prepareStatement("TRUNCATE TABLE university_people RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
        preparedStatement = connection_library_db.prepareStatement("TRUNCATE TABLE hirer, book, item RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection_library_db.close();
        connection_university_db.close();

    }

    private void setUpLoginRecognized() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        PreparedStatement ps = connection_university_db.prepareStatement("INSERT INTO university_people VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, "E256743");
        ps.setString(2, "Marco");
        ps.setString(3, "Verdi");
        ps.setString(4, "marco.verdi@studuni.com");
        ps.setString(5, "00001");
        ps.setString(6, "345234");
        ps.setString(7, "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9");
        ps.executeUpdate();

    }


    @Test
    public void testLoginHirer_SuccessAndNotFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        setUpLoginRecognized();
        hirerDAO.addHirer("E256743", "Marco", "Verdi", "marco.verdi@studuni.com", "00001");

        assertEquals(hirerDAO.getHirer("E256743"), hirerController.loginUniversityHirer("E256743", "abcd1234"));

    }



    @Test
    public void testLoginHirer_SuccessAndFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        setUpLoginRecognized();

        Hirer hirer = hirerController.loginUniversityHirer("E256743", "abcd1234");

        assertEquals(hirer, hirerDAO.getHirer("E256743"));
    }

    @Test
    public void testLoginHirer_NotRecognized() throws SQLException{
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        assertThrows(AccessDeniedException.class, () -> hirerController.loginUniversityHirer("E34212", "knvfdkjfndkjdn"));
    }

    @Test
    public void testAddToWaitingList_Success(){
        int book_code = bookDAO.addBook("titolo", LocalDate.of(2000, 6,3).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors", Library.LIBRARY_1.toString(),
                0, true);


    }

    @Test
    public void testAddToWaitingList_Fail(){
        hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
        int book_code = bookDAO.addBook("titolo", LocalDate.of(2000, 6,3).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(),
                "link", "isbn", "publishing house", 200, "authors", Library.LIBRARY_1.toString(),
                5, false);

        assertThrows(ActionDeniedException.class, () -> hirerController.addToWaitingList(bookDAO.getBook(book_code), "mail", Library.LIBRARY_1.toString()));
    }

    @Test
    public void testRegisterExternalHirer_Fail(){
        hirerDAO.addHirer("E256743", "Marco", "Verdi", "marco.verdi@studuni.com", "00001");
        Hirer hirer = hirerDAO.getHirer("E256743");
        Token token = new Token(hirer);
        hirer.setToken(token);

        assertThrows(ActionDeniedException.class, () -> hirerController.registerExternalHirer(hirer.getName(),
                hirer.getSurname(), hirer.getEmail(), hirer.getTelephoneNumber(), hirer.getToken()));

    }
}

