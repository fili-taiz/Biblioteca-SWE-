package com.progetto_swe.test_business_logic.structural_testing;

import com.progetto_swe.business_logic.LoginAdminController;
import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdmin("E256748");
        LoginAdminController loginAdminController = new LoginAdminController();

        Admin admin2 = loginAdminController.loginAdmin("E256748", "abcd1234");

        assertEquals(admin, admin2); //aggiunto nel db con successo
        assertNull(loginAdminController.loginAdmin("E256748", "abcd1234")); //non viene aggiunto nel db perché già esiste






    }


}
