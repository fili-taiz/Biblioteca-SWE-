package com.progetto_swe.orm;


import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

import java.sql.*;
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
                    + "VALUES (?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ps.setInt(3, numberOfCopies);
            ps.setBoolean(4, borrowable);
            ResultSet resultSet = ps.executeQuery(query);
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
            String query = "DELETE FROM Physical_copies P WHERE code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean updatePhysicalCopies(int code, String storagePlace, int newNumberOfCopies, boolean borrowable) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "UPDATE Physical_copies SET number_of_copies = ? WHERE code = ? AND storage_place = ? AND borrowable = ?; ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newNumberOfCopies);
            ps.setInt(2, code);
            ps.setString(3, storagePlace);
            ps.setBoolean(4, borrowable);
            if(ps.executeUpdate() <= 0){
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
            String query = "SELECT * FROM Physical_copies P WHERE P.code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet copiesSet = ps.executeQuery();
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
