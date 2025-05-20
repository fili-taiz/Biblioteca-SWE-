package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import com.progetto_swe.orm.database_exception.ConstraintViolationException;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicalCopiesDAOTest {
  Connection connection = ConnectionManager.getInstance().getConnection();
  BookDAO bookDAO = new BookDAO();
  PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();

  @BeforeEach
  public void setUp() throws SQLException {
      PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE physical_copies, book, magazine, thesis, item RESTART IDENTITY CASCADE;");
      ps.execute();
  }

  @AfterEach
  public void tearDown() throws SQLException{
      connection.close();
  }

  @Test
  public void testAddPhysicalCopies(){

      BookDAO bookDAO = new BookDAO();

      bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true);

      PhysicalCopies pc = new PhysicalCopies(5,5, true);
      HashMap<Library, PhysicalCopies> expected_pcs = new HashMap<>();
      expected_pcs.put(Library.LIBRARY_1, pc);

      assertEquals(expected_pcs, physicalCopiesDAO.getPhysicalCopies(1));
      assertThrows(IdAlreadyExistsException.class, () -> physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 5, true));

      bookDAO.addBook("titolo2", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2",  "isbn2", "publishing house 2", 300, "authors2");

      //assertThrows(ConstraintViolationException.class, () -> physicalCopiesDAO.addPhysicalCopies(2, Library.LIBRARY_1.toString(), 0, true));
  }

  @Test
  public void testRemovePhysicalCopies(){

      BookDAO bookDAO = new BookDAO();

      int book_code_1 = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1", 200, "authors1");
      physicalCopiesDAO.addPhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 5, true);


      Hirer hirer = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
      Token token = new Token(hirer);
      hirer.setToken(token);

      physicalCopiesDAO.removePhysicalCopies(1, Library.LIBRARY_1.toString());
      assertEquals(0, physicalCopiesDAO.getPhysicalCopies(1).size());

      assertThrows(IdNotFoundException.class, () -> physicalCopiesDAO.removePhysicalCopies(3, Library.LIBRARY_1.toString()));

      physicalCopiesDAO.addPhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 5, true);

      LendingDAO lendingDAO = new LendingDAO();
      lendingDAO.addLending("uc1", book_code_1, Library.LIBRARY_1.toString());
      assertThrows(ConstraintViolationException.class, () -> physicalCopiesDAO.removePhysicalCopies(1, Library.LIBRARY_1.toString()));


  }

  @Test
  public void testUpdatePhysicalCopies(){

      BookDAO bookDAO = new BookDAO();

      int book_code_1 = bookDAO.addBook("titolo1", LocalDate.of(2023,4,1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      physicalCopiesDAO.addPhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 5, true);

      physicalCopiesDAO.updatePhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 10, false);
      HashMap<Library, PhysicalCopies> expected_pcs = physicalCopiesDAO.getPhysicalCopies(book_code_1);
      PhysicalCopies pc = expected_pcs.get(Library.LIBRARY_1);

      assertEquals(10, pc.getNumberOfPhysicalCopies());
      assertFalse(pc.isBorrowable());

      assertThrows(IdNotFoundException.class, () -> physicalCopiesDAO.updatePhysicalCopies(3, Library.LIBRARY_1.toString(), 14, true));

  }

  @Test
  public void testGetPhysicalCopies(){

      bookDAO.addBook("titolo1", LocalDate.of(2023, 4, 1).toString(), Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1",  "isbn1", "publishing house 1", 200, "authors1");
      physicalCopiesDAO.addPhysicalCopies(1, Library.LIBRARY_1.toString(), 12, true);
      PhysicalCopies pc = new PhysicalCopies(12, 12, true);


      assertEquals(pc, physicalCopiesDAO.getPhysicalCopies(1).get(Library.LIBRARY_1));
  }
}
