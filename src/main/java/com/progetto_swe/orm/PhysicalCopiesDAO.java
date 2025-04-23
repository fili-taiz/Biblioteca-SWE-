package com.progetto_swe.orm;


import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PhysicalCopiesDAO {
    private Connection connection;

    public PhysicalCopiesDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public boolean addPhysicalCopies(int code, String storagePlace, int numberOfCopies, boolean borrowable){
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Physical_copies (code, storage_place, number_of_copies, borrowable)"
                    + "VALUES (" + code + ", '" + storagePlace + "', " + numberOfCopies + ", " + borrowable + " );";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing insert!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public void removePhysicalCopies(int code, String storagePlace){
        this.connection = ConnectionManager.getConnection();
        try {
            String query
                    = "DELETE FROM Physical_copies P "
                    + "WHERE code = " + code + " AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean updatePhysicalCopies(int code, String storagePlace, int newNumberOfCopies, boolean borrowable) {
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "UPDATE Physical_copies "
                    + "SET number_of_copies = " + newNumberOfCopies
                    + "WHERE code = " + code + " AND storage_place = '" + storagePlace + "' AND borrowable = " + borrowable + "; ";
            Statement statement = connection.createStatement();
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public HashMap<Library, PhysicalCopies> getPhysicalCopies(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM Physical_copies P "
                    + "WHERE P.code = " + code + ";";
            Statement statement = connection.createStatement();
            ResultSet copiesSet = statement.executeQuery(query);
            HashMap<Library, PhysicalCopies> physicalCopies = new HashMap<>();
            if (copiesSet.next()) {
                while (copiesSet.next()) {
                    physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getBoolean("borrowable")));
                }
            }
            return physicalCopies;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
