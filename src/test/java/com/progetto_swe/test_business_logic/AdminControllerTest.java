package com.progetto_swe.test_business_logic;


import com.progetto_swe.business_logic.AdminController;
import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdminControllerTest {
    Connection connection_library_db = ConnectionManager.getConnection();
    Connection connection_university_db;
    AdminDAO adminDAO = new AdminDAO();
    AdminController adminController = new AdminController();


    @BeforeEach
    public void setUp() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        PreparedStatement preparedStatement = connection_university_db.prepareStatement("TRUNCATE TABLE library_admin, university_people RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
        preparedStatement = connection_library_db.prepareStatement("TRUNCATE TABLE admin RESTART IDENTITY CASCADE;");
        preparedStatement.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection_library_db.close();
        connection_university_db.close();

    }

    private void setUpLoginRecognized() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        PreparedStatement ps = connection_university_db.prepareStatement("INSERT INTO university_people VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, "E256743");
        ps.setString(2, "Marco");
        ps.setString(3, "Verdi");
        ps.setString(4, "marco.verdi@unimail.com");
        ps.setString(5, "00001");
        ps.setString(6, "345234");
        ps.setString(7, "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9");
        ps.executeUpdate();

        ps = connection_university_db.prepareStatement("INSERT INTO library_admin VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, "E256743");
        ps.setString(2, "Marco");
        ps.setString(3, "Verdi");
        ps.setString(4, "marco.verdi@unimail.com");
        ps.setString(5, "00001");
        ps.setString(6, "LIBRARY_1");
        ps.setString(7, "345234");
        ps.setString(8, "1722c3266324344fa1dbf0c156d299a26ce14fd5d16b1f38e447da831fcaf7e9");
        ps.executeUpdate();

    }


    @Test
    public void testLoginAdmin_SuccessAndNotFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        setUpLoginRecognized();
        adminDAO.addAdmin("E256743", "Marco", "Verdi", "marco.verdi@unimail.com", "00001", "LIBRARY_1");

        assertEquals(adminDAO.getAdmin("E256743"), adminController.loginAdmin("E256743", "abcd1234"));

    }



    @Test
    public void testLoginAdmin_SuccessAndFirstLogin() throws SQLException {
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");
        setUpLoginRecognized();

        Admin admin = adminController.loginAdmin("E256743", "abcd1234");

        assertEquals(admin, adminDAO.getAdmin("E256743"));
    }

    @Test
    public void testLoginAdmin_NotRecognized() throws SQLException{
        connection_university_db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", "postgres", "filipposwe");

        assertThrows(AccessDeniedException.class, () -> adminController.loginAdmin("E34212", "knvfdkjfndkjdn"));
    }

}
