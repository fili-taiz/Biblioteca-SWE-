package com.progetto_swe.test_orm;

import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.WaitingListDAO;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class WaitingListDAOTest {
    Connection connection = ConnectionManager.getInstance().getConnection();
    WaitingListDAO waitingListDAO = new WaitingListDAO();

    @BeforeEach
    public void setUp() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("TRUNCATE TABLE waiting_list RESTART IDENTITY CASCADE;");
        ps.execute();
    }

    @AfterEach
    public void tearDown() throws SQLException{
        connection.close();
    }

    @Test
    public void testGetWaitingList(){

        ArrayList<String> emails_expected = new ArrayList<>();
        emails_expected.add("email1");
        emails_expected.add("email2");
        waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1");
        waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email2");

        assertEquals(emails_expected, waitingListDAO.getWaitingList(1, Library.LIBRARY_1.toString()));
        assertEquals(Collections.EMPTY_LIST, waitingListDAO.getWaitingList(2, Library.LIBRARY_1.toString()));

    }

    @Test
    public void testAddToWaitingList(){

        waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1");

        ArrayList<String> emails_expected = new ArrayList<>();
        emails_expected.add("email1");

        assertEquals(emails_expected, waitingListDAO.getWaitingList(1, Library.LIBRARY_1.toString()));
        assertThrows(IdAlreadyExistsException.class, () -> waitingListDAO.addToWaitingList(1, Library.LIBRARY_1.toString(), "email1"));

    }


}
