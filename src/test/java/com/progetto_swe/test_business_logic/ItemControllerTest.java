package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.ItemController;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ItemControllerTest {
   static Connection connection;
   ItemController itemController = new ItemController();
   AdminDAO adminDAO = new AdminDAO();
   BookDAO bookDAO = new BookDAO();
   ReservationDAO reservationDAO = new ReservationDAO();
   HirerDAO hirerDAO = new HirerDAO();
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
   public void setUp() throws SQLException{
       PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE book, item, hirer, reservation, admin, physical_copies RESTART IDENTITY CASCADE;");
       ps.execute();
   }

   @Test
   public void testSearchItem(){
       int book_code_1 = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
               Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1",
               "publishing house 1", 200, "authors1");
       int book_code_2 = bookDAO.addBook("Anatomia", LocalDate.of(2023,4,2).toString(),
               Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link2", "isbn2",
               "publishing house 2", 220, "authors2");

       ArrayList<Item> expected_items = new ArrayList<>();

       expected_items.add(bookDAO.getBook(book_code_1));

       ArrayList<Item> notExpected_items = new ArrayList<>();
       notExpected_items.add(bookDAO.getBook(book_code_2));

       assertEquals(expected_items, itemController.searchItem("Fond", Category.CATEGORY_1.toString()));
       assertNotEquals(notExpected_items, itemController.searchItem("Fond", Category.CATEGORY_1.toString()));


  }

  @Test
  public void testAddBook_Success(){
       adminDAO.addAdmin("uc1", "name", "surname", "mail", "09876", Library.LIBRARY_1.toString());
       Admin admin = adminDAO.getAdmin("uc1");
       Token admin_token = new Token(admin);
       admin.setToken(admin_token);

       itemController.addBook("Fondamenti di informatica", LocalDate.of(2015,2, 3).toString(), Language.LANGUAGE_1.toString(),
               Category.CATEGORY_1.toString(), "link", "isbn", "Mondadori", 300,
               "autori", 5, true, admin_token);

       Book expected_book = new Book(1, "Fondamenti di informatica", LocalDate.of(2015,2, 3), Language.LANGUAGE_1,
               Category.CATEGORY_1, "link", "isbn", "Mondadori", 300, "autori");

       assertEquals(expected_book, bookDAO.getBook(1));
   }


  @Test
  public void testAddBook_Fail(){
      Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
      Token token = new Token(hirer);
      hirer.setToken(token);

      assertThrows(ActionDeniedException.class, () -> itemController.addBook("title", LocalDate.of(2004, 2, 13).toString(),
              Language.LANGUAGE_1.toString(),
              Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200,
              "authors", 5, true, token));
  }

  @Test
  public void removeBook_Success_1(){
      adminDAO.addAdmin("uc1", "name", "surname", "mail", "09876", Library.LIBRARY_1.toString());
      Admin admin = adminDAO.getAdmin("uc1");
      Token admin_token = new Token(admin);
      admin.setToken(admin_token);
      int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2015,2, 3).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "Mondadori", 300,
                "autori");
      pcDAO.addPhysicalCopies(book_code, Library.LIBRARY_1.toString(), 5, true);
      HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(book_code);
      bookDAO.getBook(book_code).setPhysicalCopies(pcs);

      itemController.removeBook(book_code, admin_token);
      assertEquals(Collections.EMPTY_MAP, pcDAO.getPhysicalCopies(book_code));
  }

    @Test
    public void removeBook_Success_2(){
        adminDAO.addAdmin("uc1", "name", "surname", "mail", "09876", Library.LIBRARY_1.toString());
        Admin admin = adminDAO.getAdmin("uc1");
        Token admin_token = new Token(admin);
        admin.setToken(admin_token);
        int book_code = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2015,2, 3).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "", "isbn", "Mondadori", 300,
                "autori");

        itemController.removeBook(book_code, admin_token);
        assertThrows(IdNotFoundException.class, () -> bookDAO.getBook(book_code));
    }

  @Test
  public void testRemoveBook_Fail1(){
      Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
      Token hirer_token = new Token(hirer);
      hirer.setToken(hirer_token);

      int book_code_1 = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
              Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
              200, "authors1");


      assertThrows(ActionDeniedException.class, () -> itemController.removeBook(book_code_1, hirer_token));

  }

  @Test
  public void testRemoveBook_Fail2(){
      Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00000", Library.LIBRARY_1,null);
      Token admin_token = new Token(admin);
      admin.setToken(admin_token);

      itemController.addBook("Anatomia", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(),
              Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2", 1, true, admin_token);

      Admin admin_2 = new Admin("uc2", "nome2", "cognome2", "mail2", "00002", Library.LIBRARY_2,null);
      Token admin_token_2 = new Token(admin_2);
      admin.setToken(admin_token_2);


      assertThrows(ActionDeniedException.class, () -> itemController.removeBook(1, admin_token_2));

  }

  @Test
  public void testRemoveBook_Fail3(){
      Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00000", Library.LIBRARY_1,null);
      Token admin_token = new Token(admin);
      admin.setToken(admin_token);

      int book_code_1 = bookDAO.addBook("Fondamenti di informatica", LocalDate.of(2023,4,1).toString(),
              Language.LANGUAGE_1.toString(), Category.CATEGORY_1.toString(), "link1", "isbn1", "publishing house 1",
              200, "authors1");

      pcDAO.addPhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 5, true);

      HashMap<Library, PhysicalCopies> pcs = pcDAO.getPhysicalCopies(book_code_1);
      bookDAO.getBook(book_code_1).setPhysicalCopies(pcs);


      hirerDAO.addHirer("usercode", "name", "surname", "email", "00001");
      reservationDAO.addReservation("usercode", book_code_1, Library.LIBRARY_1.toString());

      assertThrows(ActionDeniedException.class, () -> itemController.removeBook(book_code_1, admin_token));

  }

  @Test
  public void testUpdateBook_Success(){
      Admin admin = new Admin("uc1", "nome", "cognome", "mail", "00000", Library.LIBRARY_1,null);
      Token admin_token = new Token(admin);
      admin.setToken(admin_token);

      int book_code_1 = bookDAO.addBook("Anatomia", LocalDate.of(2023,4,2).toString(), Language.LANGUAGE_1.toString(),
              Category.CATEGORY_1.toString(), "link2", "isbn2", "publishing house 2", 220, "authors2");
      pcDAO.addPhysicalCopies(book_code_1, Library.LIBRARY_1.toString(), 5, true);

      itemController.updateBook(book_code_1, "Programmazione", LocalDate.of(2020, 4,2).toString(), false, Language.LANGUAGE_2.toString(),
              Category.CATEGORY_2.toString(), "link3", "isbn3", "Mondadori", 250, "authors3", 8, admin_token);

      Book expected_book = new Book(book_code_1, "Programmazione", LocalDate.of(2020, 4,2), Language.LANGUAGE_2,
              Category.CATEGORY_2, "link3", "isbn3", "Mondadori", 250, "authors3");


      PhysicalCopies pc = new PhysicalCopies(8, 8, false);
      HashMap<Library, PhysicalCopies> expected_pcs = pcDAO.getPhysicalCopies(book_code_1);
      expected_pcs.put(Library.LIBRARY_2, pc);


      assertEquals(expected_book, bookDAO.getBook(book_code_1));
      assertEquals(8, pcDAO.getPhysicalCopies(book_code_1).get(Library.LIBRARY_1).getNumberOfPhysicalCopies());
      assertFalse(pcDAO.getPhysicalCopies(book_code_1).get(Library.LIBRARY_1).isBorrowable());

  }

  @Test
  public void testUpdateBook_Fail(){
      Hirer hirer = new Hirer("usercode", "name", "surname", "email", "00001", null, null);
      Token token = new Token(hirer);
      hirer.setToken(token);

      assertThrows(ActionDeniedException.class, () -> itemController.updateBook(1,"title", LocalDate.of(2004, 2, 13).toString(),
              true, Language.LANGUAGE_1.toString(),
              Category.CATEGORY_1.toString(), "link", "isbn", "publishing house", 200, "authors", 5, token));

  }
}
