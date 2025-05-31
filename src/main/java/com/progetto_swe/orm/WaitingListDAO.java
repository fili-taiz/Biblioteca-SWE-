package com.progetto_swe.orm;

import com.progetto_swe.orm.database_exception.*;

import java.sql.*;
import java.util.ArrayList;

public class WaitingListDAO {
    private Connection connection;

    public WaitingListDAO() {
        this.connection = ConnectionManager.getInstance().getConnection();
    }

    public ArrayList<String> getWaitingList(int itemCode, String storagePlace) throws DatabaseConnectionException {
        try {
            ArrayList<String> emails = new ArrayList<>();
            String query = """
                    SELECT W.email 
                    FROM waiting_list W 
                    WHERE W.code = ? AND W.storage_place = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            return emails;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void addToWaitingList(int itemCode, String storagePlace, String email)
            throws IdAlreadyExistsException, DatabaseConnectionException {
        try {
            String query = """
                    INSERT INTO waiting_list (code, storage_place, email) 
                    VALUES (?, ?, ?);
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Hirer con email [" + email + "] già in lista d'attesa.");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void removeWaitingList(int itemCode, String storagePlace) throws IdNotFoundException, DatabaseConnectionException {
        try {
            String query = """
                    DELETE FROM waiting_list L 
                    WHERE code = ? AND storage_place = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}
