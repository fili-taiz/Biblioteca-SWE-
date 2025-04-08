package com.progetto_swe.orm;


import com.progetto_swe.domain_model.PhysicalCopies;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PhysicalCopiesDAO {
    private Connection connection;

    public PhysicalCopiesDAO(Connection connection){
        this.connection = connection;
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

    public PhysicalCopies getPhysicalCopies(int code, String storagePlace) {
        try {
            String query
                    = "SELECT * "
                    + "FROM Physical_copies "
                    + "WHERE code = '" + code + "' AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            int numberOfCopies = resultSet.getInt("number_of_copies");
            boolean borrowable = resultSet.getBoolean("borrowable");

            return new PhysicalCopies(numberOfCopies, borrowable);
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
}
