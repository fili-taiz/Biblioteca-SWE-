package com.progetto_swe.orm;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
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
            // TODO Auto-generated catch block
        }
    }

    public int getPhysicalCopies(int code, String storagePlace) {
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Physical_copies "
                    + "WHERE code = '" + code + "' AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("number_of_copies");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return 0;
    }

    public boolean updatePhysicalCopies(int code, String storagePlace, int newNumberOfCopies, boolean borrowable) {
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "UPDATE Physical_copies "
                    + "SET number_of_copies = " + newNumberOfCopies
                    + "WHERE code = " + code + " AND storage_place = '" + storagePlace + "' AND borrowable = " + borrowable + "; ";
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query) == 0;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }
}
