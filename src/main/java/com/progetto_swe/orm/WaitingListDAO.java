package com.progetto_swe.orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            String query
                    = "SELECT W.email "
                    + "FROM Waiting_list W"
                    + "WHERE W.code = " + code + " AND W.storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

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
            Statement statement = connection.createStatement();
            String query
                    = "INSERT INTO Waiting_list (code, storage_place, email)"
                    + "VALUES ('" + code + "', '" + storagePlace + "', '" + email + "');";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }//si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
