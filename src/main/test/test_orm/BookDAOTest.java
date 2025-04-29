package test_orm;

import com.progetto_swe.domain_model.Book;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.orm.ConnectionManager;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class BookDAOTest {

    private static Connection connection;
    private BookDAO bookDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // carico il driver H2
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "user", "pass");
        ConnectionManager.setTestConnection(connection);

        Statement stmt = connection.createStatement();

        // creo tabelle Item e Book
        stmt.execute("CREATE TABLE IF NOT EXISTS Item (" +
                "code SERIAL, " +
                "title VARCHAR(64), " +
                "publication_date DATE, " +
                "language VARCHAR(32), " +
                "category VARCHAR(32), " +
                "link VARCHAR(32), "  +
                "number_of_pages INTEGER, " +
                "PRIMARY KEY (code))");

        stmt.execute("CREATE TABLE IF NOT EXISTS Book (" +
                "code INTEGER, " +
                "isbn VARCHAR(64), " +
                "publishing_house VARCHAR(128), " +
                "authors VARCHAR(128), " +
                "PRIMARY KEY (code)," +
                "FOREIGN KEY (code) REFERENCES Item(code))");

        // dummy per PhysicalCopiesDAO se necessario
        stmt.execute("CREATE TABLE IF NOT EXISTS Physical_Copies (" +
                "code INTEGER, " +
                "storage_place VARCHAR(64)," +
                "borrowable BOOLEAN, " +
                "PRIMARY KEY (code, storage_place), " +
                "FOREIGN KEY (code) REFERENCES Item(code));");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        connection.close();
        ConnectionManager.clearTestConnection();
    }

    @Before
    public void setUp() throws Exception {
        // Pulizia tabelle prima di ogni test
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM Book");
        stmt.execute("DELETE FROM Item");

        bookDAO = new BookDAO();
    }

    @Test
    public void testAddAndGetBook() {
        int code = bookDAO.addBook(
                "title",
                "2008-08-01",
                "LANGUAGE_1",
                "CATEGORY_1",
                "link",
                "1234",
                "publishing_house",
                500,
                "authors"
        );

        assertTrue(code > 0);

        Book book = bookDAO.getBook(code);
        assertEquals("title", book.getTitle());
        assertEquals(LocalDate.of(2008, 8, 1), book.getPublicationDate());
        assertEquals(Language.LANGUAGE_1, book.getLanguage());
        assertEquals(Category.CATEGORY_1, book.getCategory());
        assertEquals("link", book.getLink());
        assertEquals("1234", book.getIsbn());
        assertEquals("publishing_house", book.getPublishingHouse());
        assertEquals(500, book.getNumberOfPages());
        assertEquals("authors", book.getAuthors());
    }

    @Test
    public void testUpdateBook() {
        int code = bookDAO.addBook(
                "old_title", "2000-01-01", "LANGUAGE_1", "CATEGORY_2", "link", "123", "old_publishing_house", 100, "authors"
        );

        boolean newBook = bookDAO.updateBook(
                code, "new_title", "2020-05-01", "LANGUAGE_1", "CATEGORY_1", "new_link",
                "990", "new_publishing_house", "new_authors"
        );

        assertTrue(newBook);
        Book book = bookDAO.getBook(code);
        assertEquals("new_title", book.getTitle());
        assertEquals("2020-05-01", book.getPublicationDate().toString());
        assertEquals("LANGUAGE_1", book.getLanguage().toString());
        assertEquals("CATEGORY_1", book.getCategory().toString());
        assertEquals("new_link", book.getLink());
        assertEquals("990", book.getIsbn());
        assertEquals("new_publishing_house", book.getPublishingHouse());
        assertEquals(100, book.getNumberOfPages());
        assertEquals("new_authors", book.getAuthors());
    }

    @Test
    public void testRemoveExistingBook() {
        int code = bookDAO.addBook(
                "title", "2010-01-01", "LANGUAGE_1", "CATEGORY_1", "link", "isbn", "publishing_house", 100, "authors"
        );

        assertNotNull(bookDAO.getBook(code));
        assertTrue(bookDAO.removeBook(code));
    }

    @Test(expected = com.progetto_swe.orm.database_exception.CRUD_exception.class)
    public void testRemoveNonExistingBook()  {
        assertFalse(bookDAO.removeBook(123));
    }

}
