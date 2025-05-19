package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Token;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
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
    Connection connection = ConnectionManager.getConnection();
    HirerDAO hirerDAO = new HirerDAO();


    @BeforeEach
    public void setUp() throws SQLException {
        String query = "TRUNCATE TABLE banned_hirers, hirer, user_credentials RESTART IDENTITY CASCADE";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetHirer(){

        Hirer h1 = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);

        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

        assertEquals(h1, hirerDAO.getHirer("uc1"));
        assertThrows(IdNotFoundException.class, () -> hirerDAO.getHirer("uc2"));


    }

    @Test
    public void testGetSaltAndHashedPassword(){

        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        hirerDAO.addHirerPassword("uc1", "hashed_password_1", "salt_1");

        HashMap<String, String> expected = new HashMap<>();
        expected.put("salt", "salt_1");
        expected.put("hashedPassword", "hashed_password_1");

        HashMap<String, String> notExpected = new HashMap<>();
        notExpected.put("salt", "salt1");
        notExpected.put("hashedPassword", "password1");

        assertEquals(expected, hirerDAO.getSaltAndHashedPassword("uc1"));
        assertNotEquals(notExpected, hirerDAO.getSaltAndHashedPassword("uc1"));
        assertThrows(IdNotFoundException.class, () -> {
            hirerDAO.getSaltAndHashedPassword("uc2");
        });

    }

    @Test
    public void testAddHirer(){

        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");

        assertThrows(IdAlreadyExistsException.class, () -> hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1"));

    }

    @Test
    public void testAddHirerPassword(){

        hirerDAO.addHirer("uc1", "name1", "surname1", "email1", "telephonenumber1");
        hirerDAO.addHirerPassword("uc1", "hashed_password_1", "salt_1");

        assertThrows(IdAlreadyExistsException.class, () -> hirerDAO.addHirerPassword("uc1", "hp1", "salt"));


    }

    @Test
    public void testGetHirers_() throws SQLException{

        Hirer h1 = new Hirer("uc1", "name1", "surname1", "email1", "telephonenumber1", null, null);
        Token token_1 = new Token(h1);
        h1.setToken(token_1);
        Hirer h2 = new Hirer("uc2", "name2", "surname2", "email2", "telephonenumber2", null, LocalDate.of(2024, 4, 3));
        Token token_2 = new Token(h2);
        h1.setToken(token_2);
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

        assertEquals(expected.size(), hirerDAO.getHirers_().size());
        assertTrue(hirerDAO.getHirers_().containsAll(expected));

        Hirer h3 = new Hirer("uc3", "name3", "surname3", "email3", "telephonenumber3", null, LocalDate.of(2024, 4, 5));

        assertFalse(hirerDAO.getHirers_().contains(h3));

    }





}
