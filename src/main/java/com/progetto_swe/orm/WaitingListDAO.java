package com.progetto_swe.orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class WaitingListDAO {
    private Connection connection;

    public WaitingListDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public ArrayList<String> getWaitingList(int code, String storagePlace) {
        try {
            ArrayList<String> emails = new ArrayList<>();
            connection = ConnectionManager.getConnection();
            String query = "SELECT W.email FROM waiting_list W WHERE W.code = ? AND W.storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            return emails;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public boolean addToWaitingList(int code, String storagePlace, String email) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO waiting_list (code, storage_place, email) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ps.setString(3, email);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
