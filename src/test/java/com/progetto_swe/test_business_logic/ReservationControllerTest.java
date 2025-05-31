package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.ReservationController;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationControllerTest {
  ReservationController reservationController = new ReservationController();
  static Connection connection = ConnectionManager.getInstance().getConnection();
  HirerDAO hirerDAO = new HirerDAO();
  BookDAO bookDAO = new BookDAO();
  AdminDAO adminDAO = new AdminDAO();
  PhysicalCopiesDAO pcDAO = new PhysicalCopiesDAO();
  WaitingListDAO waitingListDAO = new WaitingListDAO();
  ReservationDAO reservationDAO = new ReservationDAO();
  LendingDAO lendingDAO = new LendingDAO();

    @BeforeAll
    public static void setUpBeforeClass(){
        connection = ConnectionManager.getInstance().getConnection();
    }

    @AfterAll
    public static void tearDown() throws SQLException{
        connection.close();
    }

  @BeforeEach
  public void setUp() throws Exception {
      PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE book, item, hirer, reservation, admin, physical_copies, lending RESTART IDENTITY CASCADE;");
      ps.execute();
  }

  private int setup(){
      hirerDAO.addHirer("usercode", "name", "surname", "biblioteca.SWE@gmail.com", "00001");
      return bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
              Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
              200, "authors1");
  }

  @Test
  public void testRemoveReservation_Success(){
      Book book = bookDAO.getBook(setup());
      pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, true);
      Hirer hirer = hirerDAO.getHirer("usercode");
      Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00002", Library.LIBRARY_1, null);

      Token admin_token = new Token(admin);
      admin.setToken(admin_token);

      reservationDAO.addReservation("usercode", book.getCode(), Library.LIBRARY_1.toString());

      reservationController.removeReservation(hirer, book, Library.LIBRARY_1.toString(), admin_token);

      assertTrue(reservationDAO.getReservationsByUserCode("usercode").isEmpty());
      assertTrue(waitingListDAO.getWaitingList(book.getCode(), Library.LIBRARY_1.toString()).isEmpty());

  }

  @Test
  public void testRemoveReservation_Fail1(){
      Book book = bookDAO.getBook(setup());
      Hirer hirer = hirerDAO.getHirer("usercode");
      adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
      Admin admin = adminDAO.getAdmin("usercode1");
      Token token = new Token(admin);
      admin.setToken(token);

      assertThrows(ActionDeniedException.class, () -> reservationController.removeReservation(hirer, book, Library.LIBRARY_2.toString(), token));
  }

  @Test
  public void testReserveItem_Success(){
      Book book = bookDAO.getBook(setup());
      pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, true);
      HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(1);
      book.setPhysicalCopies(pcs);
      Hirer hirer = hirerDAO.getHirer("usercode");

      Token hirer_token = new Token(hirer);
      hirer.setToken(hirer_token);

      reservationController.reserveItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token);

      ArrayList<Reservation> reservations = reservationDAO.getReservationsByUserCode("usercode");
      Reservation r = reservations.get(0);

      assertTrue(r.getHirer().equals(hirer));
      assertTrue(r.getItem().equals(book));
      assertTrue(r.getStoragePlace().equals(Library.LIBRARY_1));
      assertTrue(r.getReservationDate().equals(LocalDate.now()));
  }



  @Test
  public void testReserveItem_Fail1(){
      Book book = bookDAO.getBook(setup());
      adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
      Admin admin = adminDAO.getAdmin("usercode1");
      Token token = new Token(admin);
      admin.setToken(token);

      assertThrows(ActionDeniedException.class, () -> reservationController.reserveItem(hirerDAO.getHirer("usercode"), book,
              Library.LIBRARY_1.toString(), token));
  }

  @Test
  public void testReserveItem_Fail2(){
      Book book = bookDAO.getBook(setup());
      Hirer hirer = hirerDAO.getHirer("usercode");
      Token hirer_token = new Token(hirer);
      hirer.setToken(hirer_token);
      hirer.setUnbannedDate(LocalDate.now());

      assertThrows(ActionDeniedException.class, () -> reservationController.reserveItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token));
  }

    @Test
    public void testReserveItem_Fail_3(){
        Book book = bookDAO.getBook(setup());
        pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, false);
        HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(1);
        book.setPhysicalCopies(pcs);
        Hirer hirer = hirerDAO.getHirer("usercode");

        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);

        assertThrows(ActionDeniedException.class, () -> {reservationController.reserveItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token);});
    }

    @Test
    public void testReserveItem_Fail_4(){
        Book book = bookDAO.getBook(setup());
        pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 1, true);
        HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(1);
        book.setPhysicalCopies(pcs);
        Hirer hirer = hirerDAO.getHirer("usercode");
        Token hirer_token = new Token(hirer);
        hirer.setToken(hirer_token);

        assertThrows(ActionDeniedException.class, () -> {reservationController.reserveItem(hirer, book, Library.LIBRARY_1.toString(), hirer_token);});
    }

  @Test
  public void testConfirmReservationWithdraw_Success(){
      Book book = bookDAO.getBook(setup());
      Hirer hirer = hirerDAO.getHirer("usercode");
      pcDAO.addPhysicalCopies(book.getCode(), Library.LIBRARY_1.toString(), 5, true);
      HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(book.getCode());
      book.setPhysicalCopies(pcs);
      adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
      Admin admin = adminDAO.getAdmin("usercode1");
      Token admin_token = new Token(admin);
      admin.setToken(admin_token);

      reservationDAO.addReservation("usercode", book.getCode(), Library.LIBRARY_1.toString());

      reservationController.confirmReservationWithdraw(hirer, book, Library.LIBRARY_1.toString(), admin_token);

      assertTrue(reservationDAO.getReservationsByUserCode("usercode").isEmpty());

      ArrayList<Lending> lendings = lendingDAO.getLendingsByUserCode("usercode");

      Lending l = lendings.get(0);


      assertTrue(l.getHirer().equals(hirer));
      assertTrue(l.getItem().equals(book));
      assertTrue(l.getLendingDate().equals(LocalDate.now()));
      assertTrue(l.getMaturityDate().equals(LocalDate.now().plusMonths(1)));
      assertTrue(l.getStoragePlace().equals(Library.LIBRARY_1));

  }



  @Test
  public void testConfirmReservationWithdraw_Fail1(){
      Book book = bookDAO.getBook(setup());
      Hirer hirer = hirerDAO.getHirer("usercode");
      Token hirer_token = new Token(hirer);
      hirer.setToken(hirer_token);

      assertThrows(ActionDeniedException.class, () -> reservationController.confirmReservationWithdraw(hirer, book, Library.LIBRARY_1.toString(), hirer_token));
  }

    @Test
    public void testConfirmReservationWithdraw_Fail2(){
        Book book = bookDAO.getBook(setup());
        adminDAO.addAdmin("usercode1", "name", "surname", "mail", "00000", Library.LIBRARY_1.toString());
        Admin admin = adminDAO.getAdmin("usercode1");
        Token token = new Token(admin);
        admin.setToken(token);

        assertThrows(ActionDeniedException.class, () -> reservationController.confirmReservationWithdraw(hirerDAO.getHirer("usercode"), book,
                Library.LIBRARY_2.toString(), token));
    }



}
