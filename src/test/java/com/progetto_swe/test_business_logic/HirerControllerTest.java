package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.business_logic.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HirerControllerTest {
   Connection connection_library_db = ConnectionManager.getInstance().getConnection();
   Connection connection_university_db = ConnectionManagerUniversity.getInstance().getConnection();
   AdminDAO adminDAO = new AdminDAO();
   HirerDAO hirerDAO = new HirerDAO();
   BookDAO bookDAO = new BookDAO();
   LendingDAO lendingDAO = new LendingDAO();
   WaitingListDAO waitingListDAO = new WaitingListDAO();
   PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
   HirerController hirerController = new HirerController();
   ReservationDAO reservationDAO = new ReservationDAO();


   @BeforeEach
   public void setUp() throws SQLException {
       PreparedStatement preparedStatement = connection_university_db.prepareStatement("TRUNCATE TABLE university_people RESTART IDENTITY CASCADE;");
       preparedStatement.execute();
       preparedStatement = connection_library_db.prepareStatement("TRUNCATE TABLE hirer, book, item, reservation, lending, admin, physical_copies RESTART IDENTITY CASCADE;");
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
               Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors");

       physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 2, true);
       hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
       reservationDAO.addReservation("usercode", book_code, Library.LIBRARY_1.toString());

       Book book = bookDAO.getBook(book_code);
       HashMap<Library, PhysicalCopies> pcs = physicalCopiesDAO.getPhysicalCopies(book_code);
       book.setPhysicalCopies(pcs);

       hirerController.addToWaitingList(book, hirerDAO.getHirer("usercode").getEmail(), Library.LIBRARY_1.toString());

       ArrayList<String> emails = waitingListDAO.getWaitingList(book_code, Library.LIBRARY_1.toString());

       assertEquals(emails.size(), 1);
       assertEquals(emails.get(0), hirerDAO.getHirer("usercode").getEmail());

   }

   @Test
   public void testAddToWaitingList_Fail(){
       hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
       int book_code = bookDAO.addBook("titolo", LocalDate.of(2000, 6,3).toString(), Language.LANGUAGE_1.toString(),
               Category.CATEGORY_1.toString(),
               "link", "isbn", "publishing house", 200, "authors");

       assertThrows(ActionDeniedException.class, () -> hirerController.addToWaitingList(bookDAO.getBook(book_code), "mail", Library.LIBRARY_1.toString()));
   }

   @Test
   public void testRegisterExternalHirer_Success(){
       adminDAO.addAdmin("uc1", "name", "surname", "email", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("uc1");
       Token admin_token = new Token(admin);
       admin.setToken(admin_token);
       String expected_usercode = hirerController.registerExternalHirer("nome", "cognome", "email", "01234", admin_token);

       assertEquals(expected_usercode, hirerDAO.getHirer(expected_usercode).getUserCode());
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

   @Test
   public void testSearchHirer() {
       hirerDAO.addHirer("uc1", "Marco", "Bianchi", "marco.bianchi@unimail.com", "02121");
       hirerDAO.addHirer("uc2", "Luca", "Bianchi", "luca.bianchi@unimail.com", "09876");
       hirerDAO.addHirer("uc3", "Mario", "Rossi", "mario.rossi@unimail.com", "34563");

       ArrayList<Hirer> expected_hirers = new ArrayList<>();
       expected_hirers.add(hirerDAO.getHirer("uc1"));
       expected_hirers.add(hirerDAO.getHirer("uc2"));

       ArrayList<Hirer> notExpected_hirers = new ArrayList<>();
       notExpected_hirers.add(hirerDAO.getHirer("uc1"));
       notExpected_hirers.add(hirerDAO.getHirer("uc2"));
       notExpected_hirers.add(hirerDAO.getHirer("uc3"));

       assertEquals(expected_hirers, hirerController.searchHirer("Bianchi"));
       assertNotEquals(notExpected_hirers, hirerController.searchHirer("Bianchi"));
   }

   @Test
   public void testLoginExternalHirer_Success() throws SQLException {
       setUpLoginRecognized();
       hirerDAO.addHirer("E256743", "Marco", "Bianchi", "marco.bianchi@studuni.com", "00001");
       hirerDAO.addHirerPassword("E256743", "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9", "345234");

       assertEquals(hirerDAO.getHirer("E256743"), hirerController.loginExternalHirer("E256743", "abcd1234"));
   }

   @Test
   public void testLoginExternalHirer_Fail() throws SQLException {
       setUpLoginRecognized();
       hirerDAO.addHirer("E256743", "Marco", "Bianchi", "marco.bianchi@studuni.com", "00001");
       hirerDAO.addHirerPassword("E256743", "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9", "345234");

       assertThrows(AccessDeniedException.class, () -> hirerController.loginExternalHirer("E256743", "abcd12345"));
   }

   @Test
   public void FunctionalTestHirer() throws SQLException {
       //inizio pre-set
       int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2015, 3, 2).toString(), Language.LANGUAGE_1.toString(),
               Category.CATEGORY_1.toString(), "link", "isbn", "Mondadori", 300, "autori");

       physicalCopiesDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);

       //fine pre-set
       setUpLoginRecognized();

       Hirer hirer = hirerController.loginUniversityHirer("E256743", "abcd1234");

       Token hirer_token = new Token(hirer);
       hirer.setToken(hirer_token);

       reservationDAO.addReservation("E256743", book_code, Library.LIBRARY_1.toString());

       Reservation reservation = new Reservation(LocalDate.now(), hirer, bookDAO.getBook(book_code), Library.LIBRARY_1);

       assertEquals(reservation, reservationDAO.getReservationsByUserCode("E256743").get(0));

       ReservationController rc = new ReservationController();

      adminDAO.addAdmin("M23234", "Filippo", "Taiti", "ft0011ft@gmail.com", "034567", Library.LIBRARY_1.toString());
      Admin admin = adminDAO.getAdmin("M23234");
      Token admin_token = new Token(admin);
      HashMap<Library, PhysicalCopies> pcs = physicalCopiesDAO.getPhysicalCopies(book_code);
      Book book = bookDAO.getBook(book_code);
      book.setPhysicalCopies(pcs);

      rc.confirmReservationWithdraw(hirer, book, Library.LIBRARY_1.toString(), admin_token);

      Lending lending = new Lending(LocalDate.now(), LocalDate.now().plusMonths(1), hirer, bookDAO.getBook(book_code), Library.LIBRARY_1);

      assertTrue(reservationDAO.getReservationsByUserCode("E256743").isEmpty());
      assertEquals(lending, lendingDAO.getLendingsByUserCode("E256743").get(0));


   }


}

