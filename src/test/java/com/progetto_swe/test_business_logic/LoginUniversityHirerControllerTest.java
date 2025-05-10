package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.LoginUniversityHirerController;
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
import static org.junit.jupiter.api.Assertions.assertNull;

/* Tutti i test dei login coprono lo USE CASE 1 */

public class LoginUniversityHirerControllerTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE hirer RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    //caso in cui l'hirer è presente nel database
    @Test
    public void testLoginHirer_SuccessAndNotRecognized() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        HirerDAO hirerDAO = new HirerDAO();
        hirerDAO.addHirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001");
        Hirer hirer = hirerDAO.getHirer("7564800");
        LoginUniversityHirerController loginUniversityHirerController = new LoginUniversityHirerController();

        hirerDAO.addHirerPassword("7564800", "ed7e7a891eb6da74b01f47d6c0646e57b0742deae3f6cc841d6f942f7577f7a8", "865740");

        UserCredentials ucs = new UserCredentials("7564800", "ed7e7a891eb6da74b01f47d6c0646e57b0742deae3f6cc841d6f942f7577f7a8");

        Hirer hirer2 = new Hirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001", ucs, null);


        assertEquals(hirer, hirer2); //successo
        assertNull(loginUniversityHirerController.loginUniversityHirer("7564800", "z1x2c3v5")); //non riconosciuto perché password sbagliata

        connection.close();

    }

    //caso in cui l'hirer fa per la prima volta il login
    @Test
    public void testLoginAdmin_New() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        UserCredentials ucs = new UserCredentials("7564800", "ed7e7a891eb6da74b01f47d6c0646e57b0742deae3f6cc841d6f942f7577f7a8");
        Hirer hirer = new Hirer("7564800", "Giovanni", "Bianchi", "giovanni.bianchi@studuni.com", "10001", ucs, null);
        LoginUniversityHirerController loginUniversityHirerController = new LoginUniversityHirerController();

        Hirer hirer2 = loginUniversityHirerController.loginUniversityHirer("7564800", "d1f3g5h7");

        assertEquals(hirer, hirer2); //aggiunto nel db con successo

        connection.close();

    }
}
