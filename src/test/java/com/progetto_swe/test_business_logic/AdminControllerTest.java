package com.progetto_swe.test_business_logic;


import com.progetto_swe.business_logic.*;
import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {
    Connection connection_library_db = ConnectionManager.getInstance().getConnection();
    Connection connection_university_db;
    AdminDAO adminDAO = new AdminDAO();
    HirerDAO hirerDAO = new HirerDAO();
    BookDAO bookDAO = new BookDAO();
    PhysicalCopiesDAO pcDAO = new PhysicalCopiesDAO();
    AdminController adminController = new AdminController();
    ItemController itemController = new ItemController();
    HirerController hirerController = new HirerController();


    @BeforeEach
    public void setUp() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        PreparedStatement preparedStatement = connection_university_db.prepareStatement("TRUNCATE TABLE library_admin RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
        preparedStatement = connection_library_db.prepareStatement("TRUNCATE TABLE admin, book, physical_copies, item, hirer RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection_library_db.close();
        connection_university_db.close();

    }

    private void setUpLoginRecognized() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        PreparedStatement ps = connection_university_db.prepareStatement("INSERT INTO library_admin VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, "E256743");
        ps.setString(2, "Marco");
        ps.setString(3, "Verdi");
        ps.setString(4, "marco.verdi@unimail.com");
        ps.setString(5, "00001");
        ps.setString(6, "LIBRARY_1");
        ps.setString(7, "345234");
        ps.setString(8, "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9");
        ps.executeUpdate();

    }


    @Test
    public void testLoginAdmin_SuccessAndNotFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        //inserisco l'admin nel database dell'università
        setUpLoginRecognized();
        //inserisco l'admin nel database della biblioteca
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        //controllo che il metodo loginAdmin ritorni effettivamente l'admin corretto
        assertEquals(adminDAO.getAdmin("E256743"), adminController.loginAdmin("E256743", "abcd1234"));

    }



    //simile al test precedente, solo che ora prima dell'esecuzione di loginAdmin, l'admin non è presente nel database della biblioteca
    @Test
    public void testLoginAdmin_SuccessAndFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        setUpLoginRecognized();

        Admin admin = adminController.loginAdmin("E256743", "abcd1234");

        assertEquals(admin, adminDAO.getAdmin("E256743"));
    }

    //admin non riconosciuto dall'università
    @Test
    public void testLoginAdmin_NotRecognized() throws SQLException{
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        assertThrows(AccessDeniedException.class, () -> adminController.loginAdmin("E34212", "wrong_password"));
    }

    @Test
    public void FunctionalTestAdmin() throws SQLException {
        setUpLoginRecognized();

        Admin admin = adminController.loginAdmin("E256743", "abcd1234");
        Token admin_token = new Token(admin);

        itemController.addBook("Programmazione", LocalDate.of(2020, 3,4).toString(), Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "Mondadori", 500,
                "autori", 8, true, admin_token);

        Book expected_book = new Book(1, "Programmazione", LocalDate.of(2020, 3,4), Language.LANGUAGE_1,
                Category.CATEGORY_1, "link", "isbn", "Mondadori", 500,
                "autori");

        HashMap<Library, PhysicalCopies> expected_pcs = new HashMap<>();
        PhysicalCopies pc = new PhysicalCopies(8, 8, true);
        expected_pcs.put(Library.LIBRARY_1, pc);

        assertEquals(expected_book, bookDAO.getBook(1));
        assertEquals(expected_pcs, pcDAO.getPhysicalCopies(1));


        itemController.updateBook(1, "Fondamenti di informatica", LocalDate.of(2020, 3,4).toString(), true, Language.LANGUAGE_1.toString(),
                Category.CATEGORY_1.toString(), "link", "isbn", "Mondadori", 500,
                "autori", 8, admin_token );

        Book book = bookDAO.getBook(1);
        book.setPhysicalCopies(expected_pcs);

        assertEquals(1, book.getCode());
        assertEquals("Fondamenti di informatica", book.getTitle());
        assertEquals(LocalDate.of(2020, 3,4).toString(), book.getPublicationDate().toString());
        assertTrue(book.isBorrowable());
        assertEquals(Language.LANGUAGE_1.toString(), book.getLanguage().toString());
        assertEquals(Category.CATEGORY_1.toString(), book.getCategory().toString());
        assertEquals("link", book.getLink());
        assertEquals("isbn", book.getIsbn());
        assertEquals("Mondadori", book.getPublishingHouse());
        assertEquals(500, book.getNumberOfPages());
        assertEquals("autori", book.getAuthors());
        assertEquals(8, book.getNumberOfCopiesInLibrary(Library.LIBRARY_1));


        String usercode = hirerController.registerExternalHirer("Filippo", "Taiti", "filippotaiti@studuni.com", "00000", admin_token);

        assertEquals("Filippo", hirerDAO.getHirer(usercode).getName());
        assertEquals("Taiti", hirerDAO.getHirer(usercode).getSurname());
        assertEquals("filippotaiti@studuni.com", hirerDAO.getHirer(usercode).getEmail());
        assertEquals("00000", hirerDAO.getHirer(usercode).getTelephoneNumber());


    }

}
