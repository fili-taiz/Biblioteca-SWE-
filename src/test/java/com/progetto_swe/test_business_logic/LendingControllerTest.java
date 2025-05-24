package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.LendingController;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.*;

import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LendingControllerTest {
    LendingController lendingController = new LendingController();
    static Connection connection;
    HirerDAO hirerDAO = new HirerDAO();
    BookDAO bookDAO = new BookDAO();
    AdminDAO adminDAO = new AdminDAO();
    LendingDAO lendingDAO = new LendingDAO();
    PhysicalCopiesDAO pcDAO = new PhysicalCopiesDAO();
    WaitingListDAO waitingListDAO = new WaitingListDAO();

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
       PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE book, item, hirer, lending, admin, physical_copies RESTART IDENTITY CASCADE;");
       ps.execute();
   }

   private int setup(){
       hirerDAO.addHirer("usercode", "name", "surname", "mail1", "00001");
       return bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
               Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
               200, "authors1");
   }

   @Test
   public void registerReturnOfItem_Success(){
       Book book = bookDAO.getBook(setup());
       pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, true);
       Hirer hirer = hirerDAO.getHirer("usercode");
       Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00002", Library.LIBRARY_1, null);

       Token admin_token = new Token(admin);
       admin.setToken(admin_token);

       lendingDAO.addLending("usercode", book.getCode(), Library.LIBRARY_1.toString());
       waitingListDAO.addToWaitingList(book.getCode(), Library.LIBRARY_1.toString(), hirer.getEmail());
       lendingController.registerReturnOfItem(hirer, book, Library.LIBRARY_1.toString(), admin_token);


       assertTrue(lendingDAO.getLendingsByUserCode("usercode").isEmpty());
       assertTrue(waitingListDAO.getWaitingList(book.getCode(), Library.LIBRARY_1.toString()).isEmpty());
   }


   @Test
   public void registerReturnOfItem_Fail1(){
       Book book = bookDAO.getBook(setup());
       Hirer hirer = hirerDAO.getHirer("usercode");
       Token hirer_token = new Token(hirer);
       hirer.setToken(hirer_token);

       assertThrows(ActionDeniedException.class, () -> lendingController.registerReturnOfItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token));
   }

   @Test
   public void registerReturnOfItem_Fail2(){
       Book book = bookDAO.getBook(setup());
       adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("usercode1");
       Token token = new Token(admin);
       admin.setToken(token);
       Hirer hirer = hirerDAO.getHirer("usercode");
       assertThrows(ActionDeniedException.class, () -> lendingController.registerReturnOfItem(hirer, book, Library.LIBRARY_2.toString(), token));
   }

   @Test
   public void registerLending_Success(){
       Book book = bookDAO.getBook(setup());
       pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, true);
       HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(book.getCode());
       book.setPhysicalCopies(pcs);
       adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("usercode1");
       Token token = new Token(admin);
       admin.setToken(token);
       Hirer hirer = hirerDAO.getHirer("usercode");
       lendingController.registerLending(hirer, book, token);

       ArrayList<Lending> lendings = lendingDAO.getLendingsByUserCode("usercode");

       Lending l = lendings.get(0);

       assertTrue(l.getHirer().equals(hirer));
       assertTrue(l.getLendingDate().equals(LocalDate.now()));
       assertTrue(l.getMaturityDate().equals(LocalDate.now().plusMonths(1)));
       assertTrue(l.getItem().equals(book));
       assertTrue(l.getStoragePlace().equals(Library.LIBRARY_1));
   }

   @Test
   public void registerLending_Fail1(){
       Book book = bookDAO.getBook(setup());
       Hirer hirer = hirerDAO.getHirer("usercode");
       Token hirer_token = new Token(hirer);
       hirer.setToken(hirer_token);

       assertThrows(ActionDeniedException.class, () -> lendingController.registerLending(hirer, book, hirer_token));
   }

   @Test
   public void registerLending_Fail2(){
       Book book = bookDAO.getBook(setup());
       Hirer hirer = hirerDAO.getHirer("usercode");
       adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("usercode1");
       Token token = new Token(admin);
       admin.setToken(token);
       hirer.setUnbannedDate(LocalDate.now());

       assertThrows(ActionDeniedException.class, () -> lendingController.registerLending(hirer, book, token));
   }

   @Test
   public void registerLending_Fail3(){
       hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
       int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
               Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
               200, "authors1");
       Hirer hirer = hirerDAO.getHirer("usercode");
       adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("usercode1");
       Token token = new Token(admin);
       admin.setToken(token);

       assertThrows(ActionDeniedException.class, () -> lendingController.registerLending(hirer, bookDAO.getBook(book_code), token));
   }

   @Test
   public void registerLending_Fail4(){
       hirerDAO.addHirer("usercode", "name", "surname", "mail", "00001");
       int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
               Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
               200, "authors1");
       pcDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 2, true);
       Hirer hirer = hirerDAO.getHirer("usercode");
       adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("usercode1");
       Token token = new Token(admin);
       admin.setToken(token);

       ReservationDAO reservationDAO = new ReservationDAO();
       reservationDAO.addReservation("usercode", book_code, Library.LIBRARY_1.toString());

       assertThrows(ActionDeniedException.class, () -> lendingController.registerLending(hirer, bookDAO.getBook(book_code), token));
   }
}
