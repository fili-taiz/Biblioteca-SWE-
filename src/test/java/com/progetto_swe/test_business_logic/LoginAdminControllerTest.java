package com.progetto_swe.test_business_logic;

import com.progetto_swe.business_logic.LoginAdminController;
import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.UserCredentials;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/* Tutti i test dei login coprono lo USE CASE 1 */

public class LoginAdminControllerTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE admin RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    //caso in cui l'admin è presente nel database
    @Test
    public void testLoginAdmin_SuccessAndNotRecognized() throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");
        Admin admin = adminDAO.getAdmin("E256743");
        LoginAdminController loginAdminController = new LoginAdminController();

        Admin admin2 = loginAdminController.loginAdmin("E256743", "a1s2d3f4");

        assertEquals(admin, admin2); //successo
        assertNull(loginAdminController.loginAdmin("E256743", "a1s2d3f5")); //non riconosciuto perché password sbagliata

        connection.close();

    }

    //caso in cui l'admin fa per la prima volta il login
    @Test
    public void testLoginAdmin_New() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        UserCredentials ucs = new UserCredentials("E256748", "bdf132ee087492615a5344c81516c1bfeb668000d799e209234e8f9666ea4948");
        Admin admin = new Admin("E256748", "Francesco", "Gialli", "francesco.gialli@unimail.com", "00005", Library.LIBRARY_1, ucs);
        LoginAdminController loginAdminController = new LoginAdminController();

        Admin admin2 = loginAdminController.loginAdmin("E256748", "abcd1234");

        assertEquals(admin, admin2); //aggiunto nel db con successo

        connection.close();

    }


}
