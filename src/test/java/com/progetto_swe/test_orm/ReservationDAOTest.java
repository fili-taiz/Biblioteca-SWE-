package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ReservationDAOTest {
  static Connection connection;
  ReservationDAO reservationDAO = new ReservationDAO();
  PhysicalCopiesDAO pcDAO = new PhysicalCopiesDAO();

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
      PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE reservation, hirer, physical_copies, book, magazine, item RESTART IDENTITY CASCADE;");
      ps.execute();
  }

  @Test
  public void testGetReservationsByUserCode(){

      Book book = new Book(1, "titolo1", LocalDate.of(2023,4,1), Language.LANGUAGE_1, Category.CATEGORY_1, "link1", "isbn1", "publishing house 1", 200, "authors1" );
      Magazine magazine = new Magazine(2, "titolo2", LocalDate.of(2023,4,7), Language.LANGUAGE_1, Category.CATEGORY_1, "link2", 50, "publishing house 2");
      BookDAO bookDAO = new BookDAO();
      MagazineDAO magazineDAO = new MagazineDAO();
      int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      pcDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);
      int magazine_code = magazineDAO.addMagazine("titolo2", LocalDate.of(2023,4,7).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "publishing house 2", 50);
      pcDAO.addPhysicalCopies(magazine_code, Library.LIBRARY_2.toString(), 5, true);

      HirerDAO hirerDAO = new HirerDAO();
      Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
      Token token = new Token(hirer);
      hirer.setToken(token);
      hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

      reservationDAO.addReservation("uc1", book_code, Library.LIBRARY_1.toString());
      reservationDAO.addReservation("uc1", magazine_code, Library.LIBRARY_2.toString());

      ArrayList<Reservation> expected_reservations = new ArrayList<>();

      Reservation r1 = new Reservation(LocalDate.now(), hirer, book, Library.LIBRARY_1);
      Reservation r2 = new Reservation(LocalDate.now(), hirer, magazine, Library.LIBRARY_2);

      expected_reservations.add(r1);
      expected_reservations.add(r2);

      assertEquals(expected_reservations.size(), reservationDAO.getReservationsByUserCode("uc1").size());
      assertTrue(reservationDAO.getReservationsByUserCode("uc1").containsAll(expected_reservations));


      assertTrue(reservationDAO.getReservationsByUserCode("uc2").isEmpty());


  }

  @Test
  public void testAddReservation(){

      BookDAO bookDAO = new BookDAO();
      int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      pcDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);
      HirerDAO hirerDAO = new HirerDAO();
      hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

      reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString());

      Reservation r = reservationDAO.getReservationsByUserCode("uc1").get(0);

      assertEquals(hirerDAO.getHirer("uc1"), r.getHirer());
      assertEquals(bookDAO.getBook(book_code), r.getItem());
      assertEquals(LocalDate.now(), r.getReservationDate());
      assertEquals(Library.LIBRARY_1, r.getStoragePlace());

      assertThrows(IdAlreadyExistsException.class, () -> reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString()));

  }

  @Test
  public void testRemoveReservation(){

      BookDAO bookDAO = new BookDAO();
      int book_code = bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      pcDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);
      HirerDAO hirerDAO = new HirerDAO();
      hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

      reservationDAO.addReservation("uc1", 1, Library.LIBRARY_1.toString());

      reservationDAO.removeReservation("uc1", book_code, Library.LIBRARY_1.toString());

      assertTrue(reservationDAO.getReservationsByUserCode("uc1").isEmpty());
      assertThrows(IdNotFoundException.class, () -> reservationDAO.removeReservation("uc2", 1, Library.LIBRARY_1.toString()));

  }
}
