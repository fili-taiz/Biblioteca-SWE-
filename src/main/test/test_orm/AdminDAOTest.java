package test_orm;
import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.database_exception.DataAccessException;
import org.junit.*;
import java.sql.*;
import static org.junit.Assert.*;

public class AdminDAOTest {

    private static Connection connection;
    private AdminDAO adminDao;

    @BeforeClass
    public static void setUpClass() throws Exception{
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "filippo", "123");

        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Admin(" +
                "user_code VARCHAR(32)," +
                "name VARCHAR(32)," +
                "surname VARCHAR(32)," +
                "email VARCHAR(128) UNIQUE," +
                "telephone_number VARCHAR(32) UNIQUE," +
                "working_place VARCHAR(128)," +
                "PRIMARY KEY(user_code))");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        connection.close();
    }

    @Before
    public void setUp() throws Exception{
        connection.createStatement().execute("DELETE FROM Admin");

        PreparedStatement ps = connection.prepareStatement("INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place) VALUES ('usercode', 'name', 'surname', 'email', '00000', 'LIBRARY_1')");
        ps.executeUpdate();

        adminDao = new AdminDAO(connection);
    }

    @Test
    public void testGetAdminFound(){
        Admin admin = adminDao.getAdmin("usercode");
        assertNotNull(admin);
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals("LIBRARY_1", admin.getWorkingPlace().toString());
    }

    @Test(expected = DataAccessException.class)
    public void testGetAdminNotFound(){
        Admin admin = adminDao.getAdmin("code");
    }

    @Test
    public void addAdmin() throws Exception{ //l'admin viene già aggiunto con setUp()
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place) VALUES ('usercode_1', 'name_1', 'surname_1', 'email_1', '11111', 'LIBRARY_1')");
        ps.executeUpdate();
        Admin admin = adminDao.getAdmin("usercode_1");
        assertNotNull(admin);
        assertEquals("name_1", admin.getName());
        assertEquals("surname_1", admin.getSurname());
        assertEquals("email_1", admin.getEmail());
        assertEquals("11111", admin.getTelephoneNumber());
        assertEquals("LIBRARY_1", admin.getWorkingPlace().toString());


    }
}
