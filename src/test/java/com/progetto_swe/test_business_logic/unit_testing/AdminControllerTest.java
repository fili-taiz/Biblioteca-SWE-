package com.progetto_swe.test_business_logic.unit_testing;

import com.progetto_swe.business_logic.AdminController;
import com.progetto_swe.business_logic.Hasher;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminControllerTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE admin, hirer, book, magazine, thesis, item, physical_copies RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testConstructor(){
        UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
        Admin admin = new Admin("usercode", "name", "surname", "email", "00000", Library.LIBRARY_1, ucs);
        assertEquals("usercode", admin.getUserCode());
        assertEquals("name", admin.getName());
        assertEquals("surname", admin.getSurname());
        assertEquals("email", admin.getEmail());
        assertEquals("00000", admin.getTelephoneNumber());
        assertEquals(Library.LIBRARY_1, admin.getWorkingPlace());
        assertEquals(ucs, admin.getUserCredentials());
    }

    @Test
    public void testRegisterExternalHirer() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        try{
            UserCredentials ucs = new UserCredentials("usercode", "hashed_password");
            Admin admin = new Admin("usercode", "name", "surname", "admin@mail.com", "00000", Library.LIBRARY_1, ucs);
            AdminController adminController = new AdminController(admin);
            boolean res = adminController.registerExternalHirer("password", "name", "surname", "admin@mail.com", "00000");
            assertTrue(res);

            HirerDAO hirerDAO = new HirerDAO();
            ListOfHirers listOfHirers = hirerDAO.getHirers_();

            Optional<Hirer> exp = listOfHirers.getHirers().stream().filter(h -> h.getEmail().equals("admin@mail.com") && h.getUserCode().startsWith("E")).findFirst();
            assertTrue(exp.isPresent());

            String query = "SELECT salt, hashed_password FROM user_credentials WHERE user_code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, exp.get().getUserCode());
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            String salt = rs.getString("salt");
            String hashedPassword = rs.getString("hashed_password");

            String recalculation = Hasher.hash("password", salt);
            assertEquals(recalculation, hashedPassword);



        }finally{
            connection.close();
        }

    }


}
