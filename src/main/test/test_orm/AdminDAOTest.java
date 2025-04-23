package test_orm;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;
import org.junit.*;

import java.sql.*;

import static org.junit.Assert.*;

public class AdminDAOTest {

    private static Connection connection;
    private AdminDAO adminDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Caricamento del driver H2
        Class.forName("org.h2.Driver");

        // Connessione a database in memoria
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        // Creazione tabella Admin
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Admin(" +
                "user_code VARCHAR(32)," +
                "name VARCHAR(32)," +
                "surname VARCHAR(32)," +
                "email VARCHAR(128) UNIQUE," +
                "telephone_number VARCHAR(32) UNIQUE," +
                "working_place VARCHAR(128)," +
                "PRIMARY KEY(user_code)" +
                ");");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ConnectionManager.clearTestConnection(); // resetta la connessione test
        connection.close();
    }

    @Before
    public void setUp() throws Exception {
        connection.createStatement().execute("DELETE FROM Admin");

        Statement insert = connection.createStatement();
        insert.execute("INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place) " +
                "VALUES ('02345', 'Mario', 'Rossi', 'mario.rossi@unifi.it', '1234', 'LIBRARY_1')");

        // Inietta la connessione H2 nel ConnectionManager
        ConnectionManager.setTestConnection(connection);

        // Ora adminDAO userà la connessione di test
        adminDAO = new AdminDAO();
    }

    @Test
    public void testGetAdminFound() {
        Admin admin = adminDAO.getAdmin("02345");

        assertNotNull(admin);
        assertEquals("Mario", admin.getName());
        assertEquals("Rossi", admin.getSurname());
        assertEquals("mario.rossi@unifi.it", admin.getEmail());
        assertEquals("1234", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
    }

    @Test(expected = com.progetto_swe.orm.database_exception.DataAccessException.class)
    public void testGetAdminNotFound() {
        adminDAO.getAdmin("56789");
    }

    @Test
    public void testAddAdmin() {
        boolean result = adminDAO.addAdmin("01234", "Filippo", "Taiti", "filippo.taiti@unifi.it", "0987654321", "LIBRARY_2");
        assertTrue(result);

        Admin admin = adminDAO.getAdmin("01234");
        assertNotNull(admin);
        assertEquals("Filippo", admin.getName());
        assertEquals(Library.LIBRARY_2, admin.getWorkingPlace());
    }
}
