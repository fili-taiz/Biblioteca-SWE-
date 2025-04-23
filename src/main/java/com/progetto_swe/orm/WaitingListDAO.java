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
            String query = "SELECT W.email FROM Waiting_list W WHERE W.code = ? AND W.storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ResultSet resultSet = ps.executeQuery(query);

            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }

            while(resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            return emails;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addToWaitingList(int code, String storagePlace, String email) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Waiting_list (code, storage_place, email) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ps.setString(3, email);
            if(ps.executeUpdate() <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }//si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
