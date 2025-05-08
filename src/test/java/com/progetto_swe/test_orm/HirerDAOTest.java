package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.ListOfHirers;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.database_exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HirerDAOTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        String query = "TRUNCATE TABLE banned_hirers, hirer, user_credentials RESTART IDENTITY CASCADE";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }


    /* Siccome un Hirer può essere aggiunto nella tabella banned_hirers solo manualmente da parte di un admin e il metodo getHirer() coinvolge anche tale tabella,
    * nell'esecuzione del seguente test si considera che sia stato inserito manualmente all'interno di tale tabella l'hirer H2, come riportato sotto.
    * Siccome c'è un vincolo di integrità referenziale, è necessario prima andare ad inserire l'Hirer H2 nella tabella Hirer, dunque faccio il commit.
    * Naturalmente poi, al termine del test, svuoterò entrambe le tabelle.*/

    @Test
    public void testGetHirer() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        Hirer h1 = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
        Hirer h2 = new Hirer("uc2", "name2", "surname2", "email2", "telephonenumber2", null, LocalDate.of(2024, 4, 3));


        HirerDAO hirerDAO = new HirerDAO();

        hirerDAO.addHirer("uc2", "name2", "surname2", "email2", "telephonenumber2");

        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

        assertEquals(h1, hirerDAO.getHirer("uc1"));
        String query = "INSERT INTO banned_hirers(user_code, unbanned_date) VALUES (?, ?);";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, "uc2");
        ps.setDate(2, Date.valueOf(LocalDate.of(2024, 4, 3)));
        ps.executeUpdate();
        assertEquals(h2, hirerDAO.getHirer("uc2"));
        assertNull(hirerDAO.getHirer("uc3"));

        String query_2 = "DELETE FROM banned_hirers WHERE user_code = ?;";
        PreparedStatement ps_2 = connection.prepareStatement(query_2);
        ps_2.setString(1, "uc2");
        ps_2.executeUpdate();

        connection.close();

    }

    @Test
    public void testGetSaltAndHashedPassword() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        hirerDAO.addHirerPassword("uc1", "password", "salt");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("salt", "salt");
        expected.put("hashedPassword", "password");

        HashMap<String, String> notExpected = new HashMap<>();
        notExpected.put("salt", "salt1");
        notExpected.put("hashedPassword", "password1");

        assertEquals(expected, hirerDAO.getSaltAndHashedPassword("uc1"));
        assertNotEquals(notExpected, hirerDAO.getSaltAndHashedPassword("uc1"));
        assertThrows(DataAccessException.class, () -> {
            hirerDAO.getSaltAndHashedPassword("uc2");
        });

        connection.close();

    }

    @Test
    public void testAddHirer() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        assertTrue(hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1"));
        assertFalse(hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1"));

        connection.close();

    }

    @Test
    public void testAddHirerPassword() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        assertTrue(hirerDAO.addHirerPassword("uc1", "hp1", "salt"));
        assertFalse(hirerDAO.addHirerPassword("uc2", "hp2", "salt"));

        connection.close();

    }

    @Test
    public void testGetHirers_() throws SQLException{
        Connection connection = ConnectionManager.getConnection();

        Hirer h1 = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
        Hirer h2 = new Hirer("uc2", "name2", "surname2", "email2", "telephonenumber2", null, LocalDate.of(2024, 4, 3));
        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        hirerDAO.addHirer("uc2", "name2", "surname2", "email2", "telephonenumber2");
        String query = "INSERT INTO banned_hirers(user_code, unbanned_date) VALUES (?, ?);";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, "uc2");
        ps.setDate(2, Date.valueOf(LocalDate.of(2024,4,3)));
        ps.executeUpdate();
        ArrayList<Hirer> expected = new ArrayList<>();
        expected.add(h1);
        expected.add(h2);
        ListOfHirers listOfHirers_e = new ListOfHirers(expected);

        assertEquals(listOfHirers_e.getHirers().size(), hirerDAO.getHirers_().getHirers().size());
        assertTrue(hirerDAO.getHirers_().getHirers().containsAll(listOfHirers_e.getHirers()));

        Hirer h3 = new Hirer("uc3", "name3", "surname3", "email3", "telephonenumber3", null, LocalDate.of(2024, 4, 5));

        assertFalse(hirerDAO.getHirers_().getHirers().contains(h3));

        connection.close();

    }





}
