package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.LoginExternalHirerController;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/* Tutti i test dei login coprono lo USE CASE 1 */

public class LoginExternalHirerControllerTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE hirer RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testLoginExternalHirer_Success() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7657382", "Mario", "Rossi", "mario.rossi@mail.com", "03400");
        hirerDAO.addHirerPassword("7657382", "2e1e47f4c48f13c8d8e43957d5578af8ec128764134875dd400f6a2ae34d858b", "605674");
        LoginExternalHirerController loginExternalHirerController = new LoginExternalHirerController();

        UserCredentials ucs = new UserCredentials("7657382", "2e1e47f4c48f13c8d8e43957d5578af8ec128764134875dd400f6a2ae34d858b");

        Hirer hirer = new Hirer("7657382", "Mario", "Rossi", "mario.rossi@mail.com", "03400", ucs, null);

        assertEquals(hirer, loginExternalHirerController.loginExternalHirer("7657382", "z1x2c3v4"));

        connection.close();
    }

    @Test
    public void testLoginExternalHirer_WrongPassword() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7657382", "Mario", "Rossi", "mario.rossi@mail.com", "03400");
        hirerDAO.addHirerPassword("7657382", "2e1e47f4c48f13c8d8e43957d5578af8ec128764134875dd400f6a2ae34d858b", "605674");
        LoginExternalHirerController loginExternalHirerController = new LoginExternalHirerController();

        UserCredentials ucs = new UserCredentials("7657382", "2e1e47f4c48f13c8d8e43957d5578af8ec128764134875dd400f6a2ae34d858b");

        Hirer hirer = new Hirer("7657382", "Mario", "Rossi", "mario.rossi@mail.com", "03400", ucs, null);

        assertNotEquals(hirer, loginExternalHirerController.loginExternalHirer("7657382", "z1x2c3v5"));

        connection.close();

    }
}
