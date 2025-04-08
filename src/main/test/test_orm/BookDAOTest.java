package test_orm;

import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.PhysicalCopies;
import com.progetto_swe.orm.BookDAO;
import com.progetto_swe.domain_model.Book;
import com.progetto_swe.orm.PhysicalCopiesDAO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.Assert.*;

public class BookDAOTest {
    private static Connection connection;
    private BookDAO bookDao;
    private PhysicalCopiesDAO pcDAO;

    @BeforeClass
    public static void setUpClass() throws Exception{
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "filippo", "123");

        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS Item(" +
                "code SERIAL," +
                "title VARCHAR(64)," +
                "publication_date DATE," +
                "language VARCHAR(32)," +
                "category VARCHAR(32)," +
                "link VARCHAR(32)," +
                "number_of_pages INTEGER," +
                "PRIMARY KEY(code)" +
                ");");

        statement.execute("CREATE TABLE IF NOT EXISTS Book(" +
                "code INTEGER," +
                "isbn VARCHAR (64)," +
                "publishing_house VARCHAR(128)," +
                "number_of_pages INTEGER," +
                "authors VARCHAR(128)," +
                "PRIMARY KEY(code)," +
                "FOREIGN KEY (code) REFERENCES Item (code)" +
                ");");
        statement.execute("CREATE TABLE IF NOT EXISTS Physical_Copies(" +
                "code INTEGER," +
                "storage_place VARCHAR (64)," +
                "number_of_copies INTEGER," +
                "borrowable BOOLEAN, " +
                "PRIMARY KEY(code, storage_place)," +
                "FOREIGN KEY (code) REFERENCES Item (code)" +
                ");");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        connection.close();
    }

    @Before
    public void setUp() throws Exception{
        connection.createStatement().execute("DELETE FROM Item");
        connection.createStatement().execute("DELETE FROM Book");
        connection.createStatement().execute("DELETE FROM Physical_Copies");

        PreparedStatement ps = connection.prepareStatement("INSERT INTO Item(title, publication_date, language, category, link, number_of_pages) VALUES ('title', '2024-05-04', 'LANGUAGE_1', 'CATEGORY_1', 'link', 100)");
        ps.executeUpdate();
        ps = connection.prepareStatement("INSERT INTO Book (code, isbn, publishing_house, number_of_pages, authors) VALUES (1, '12345', 'publishing_house', 100, 'authors')");
        ps.executeUpdate();
        ps = connection.prepareStatement("INSERT INTO Physical_Copies(code, storage_place, number_of_copies, borrowable) VALUES (1, 'LIBRARY_1', 100, TRUE)");
        ps.executeUpdate();

        bookDao = new BookDAO(connection);
        pcDAO = new PhysicalCopiesDAO(connection);
    }

    @Test
    public void testGetBook(){
        Book book = bookDao.getBook(1);
        assertNotNull(book);
        assertEquals(1, book.getCode());
        assertEquals("12345", book.getIsbn());
        assertEquals("publishing_house", book.getPublishingHouse());
        assertEquals(100, book.getNumberOfPages());
        assertEquals("authors", book.getAuthors());

        PhysicalCopies pc = pcDAO.getPhysicalCopies(1, Library.LIBRARY_1.toString());
        assertNotNull(pc);
        assertEquals(100, pc.getNumberOfPhysicalCopies());
        assertTrue(pc.isBorrowable());
    }
}
