package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Token;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.*;


import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AdminDAOTest {
    Connection connection = ConnectionManager.getInstance().getConnection();
    AdminDAO adminDAO = new AdminDAO();


    @BeforeEach
    public void setUp() throws SQLException{
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE admin RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }


    @Test
    public void testGetAdmin() throws SQLException {

        String query = "INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, "123");
        ps.setString(2, "Filippo");
        ps.setString(3, "Taiti");
        ps.setString(4, "filippo.taiti@edu.unifi.it");
        ps.setString(5, "333");
        ps.setString(6, Library.LIBRARY_1.toString());
        ps.executeUpdate();

        Admin admin = adminDAO.getAdmin("123");

        assertNotNull(admin);
        assertEquals(admin.getUserCode(), "123");
        assertEquals(admin.getName(), "Filippo");
        assertEquals(admin.getSurname(), "Taiti");
        assertEquals(admin.getEmail(), "filippo.taiti@edu.unifi.it");
        assertEquals(admin.getTelephoneNumber(), "333");
        assertEquals(admin.getWorkingPlace(), Library.LIBRARY_1);

        assertThrows(IdNotFoundException.class, () -> adminDAO.getAdmin("456"));

    }

   @Test
    public void testAddAdmin(){
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.addAdmin("uc1", "name", "surname", "email", "00000", Library.LIBRARY_1.toString());

        assertThrows(IdAlreadyExistsException.class, () -> adminDAO.addAdmin("uc1", "name", "surname", "email", "00000", Library.LIBRARY_1.toString()));

        Admin new_Admin = new Admin("uc2", "nome", "surname", "email", "1234567890", Library.LIBRARY_2, null);
        new_Admin.setToken(new Token(new_Admin));
        adminDAO.addAdmin("uc2", "nome", "surname", "email", "1234567890", Library.LIBRARY_2.toString());
        Admin inserted_Admin = adminDAO.getAdmin("uc2");
        assertEquals(new_Admin, inserted_Admin);

    }





}
