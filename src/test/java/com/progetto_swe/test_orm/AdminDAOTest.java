package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.AdminDAO;
import com.progetto_swe.orm.ConnectionManager;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.*;

import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;


public class AdminDAOTest {

    @BeforeEach
    public void setUp() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE admin RESTART IDENTITY CASCADE;");
        ps.execute();
    }


    @Test
    public void testGetAdmin() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);

        try {
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

            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getAdmin("123");

            assertNotNull(admin);
            assertEquals(admin.getUserCode(), "123");
            assertEquals(admin.getName(), "Filippo");
            assertEquals(admin.getSurname(), "Taiti");
            assertEquals(admin.getEmail(), "filippo.taiti@edu.unifi.it");
            assertEquals(admin.getTelephoneNumber(), "333");
            assertEquals(admin.getWorkingPlace(), Library.LIBRARY_1);

            assertThrows(DataAccessException.class, () -> {adminDAO.getAdmin("456");});

            connection.rollback();

        }finally {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        }

    }

   @Test
    public void testAddAdmin() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        connection.setAutoCommit(false);
        try {
            String query = "INSERT INTO Admin (user_code, name, surname, email, telephone_number, working_place)"
                    + " VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "123");
            ps.setString(2, "Filippo");
            ps.setString(3, "Taiti");
            ps.setString(4, "filippo.taiti@edu.unifi.it");
            ps.setString(5, "333");
            ps.setString(6, Library.LIBRARY_1.toString());
            assertEquals(1, ps.executeUpdate());

            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getAdmin("123");

            assertNotNull(admin);
            assertEquals(admin.getUserCode(), "123");
            assertEquals(admin.getName(), "Filippo");
            assertEquals(admin.getSurname(), "Taiti");
            assertEquals(admin.getEmail(), "filippo.taiti@edu.unifi.it");
            assertEquals(admin.getTelephoneNumber(), "333");
            assertEquals(admin.getWorkingPlace(), Library.LIBRARY_1);

            ps = connection.prepareStatement(query);
            ps.setString(1, "123");
            ps.setString(2, "Marco");
            ps.setString(3, "Taiti");
            ps.setString(4, "marco.bianchi@edu.unifi.it");
            ps.setString(5, "334");
            ps.setString(6, Library.LIBRARY_1.toString());
            assertThrows(PSQLException.class, ps::executeUpdate);

            connection.rollback();

        }finally{
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        }
    }




}
