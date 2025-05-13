package com.progetto_swe.orm;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class WaitingListDAO {
    private Connection connection;

    public WaitingListDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public ArrayList<String> getWaitingList(int itemCode, String storagePlace) throws DatabaseConnectionException {
        try {
            ArrayList<String> emails = new ArrayList<>();
            connection = ConnectionManager.getConnection();
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
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void addToWaitingList(int itemCode, String storagePlace, String email)
            throws IdAlreadyExistsException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
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
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void removeWaitingList(int itemCode, String storagePlace) throws IdNotFoundException, DatabaseConnectionException, SQLException {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = """
                    DELETE FROM waiting_list L 
                    WHERE code = ? AND storage_place = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            if(ps.executeUpdate() == 0) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Non c'è nessuno in attesa dell'item con itemCode [" + itemCode + "] ");
            }
        } catch (SQLException e) {
            connection.rollback();
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }
}
