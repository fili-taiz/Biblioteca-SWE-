package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.WaitingListDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class WaitingListDAOTest {

    @BeforeEach
    public void setUp() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE waiting_list RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @Test
    public void testGetWaitingList() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        try{
            ArrayList<String> emails_expected = new ArrayList<>();
            emails_expected.add("email1");
            emails_expected.add("email2");
            WaitingListDAO waitingListDAO = new WaitingListDAO();
            waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1");
            waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email2");
            assertEquals(emails_expected, waitingListDAO.getWaitingList(1, Library.LIBRARY_1.toString()));
            assertEquals(Collections.EMPTY_LIST, waitingListDAO.getWaitingList(2, Library.LIBRARY_1.toString()));

        }finally{
            connection.close();
        }
    }

    @Test
    public void testAddToWaitingList() throws SQLException{
        Connection connection = ConnectionManager.getConnection();
        try{
            WaitingListDAO waitingListDAO = new WaitingListDAO();
            assertTrue(waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1"));
            assertFalse(waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1"));


        }finally{
            connection.close();
        }
    }

}
