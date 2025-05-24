package com.progetto_swe.orm;


import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

import java.sql.*;
import java.util.HashMap;

public class PhysicalCopiesDAO {
    private Connection connection;

    public PhysicalCopiesDAO(){
        this.connection = ConnectionManager.getInstance().getConnection();
    }

    public void addPhysicalCopies(int itemCode,
                                     String storagePlace,
                                     int numberOfCopies,
                                     boolean borrowable)
            throws ConstraintViolationException, DatabaseConnectionException {
        try {
            String query = """ 
                    INSERT INTO physical_copies (code, storage_place, number_of_copies, borrowable, number_of_available_copies) 
                    VALUES (?, ?, ?, ?, ?);
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            ps.setInt(3, numberOfCopies);
            ps.setBoolean(4, borrowable);
            ps.setInt(5, numberOfCopies);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Articolo con userCode [" + itemCode + "] già presente nella sede [" + storagePlace + "].");
            }
            if(e.getSQLState().equals("23514")){
                throw new ConstraintViolationException("Errore: Non puoi avere 0 copie di un Item.");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void removePhysicalCopies(int itemCode, String storagePlace)
            throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        try {
            String query = """
                    DELETE FROM physical_copies P 
                    WHERE code = ? AND storage_place = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: L'Item con itemCode [" + itemCode + "] non ha copie presso la sede [" + storagePlace + "].");
            }

            WaitingListDAO waitingListDAO = new WaitingListDAO();
            waitingListDAO.removeWaitingList(itemCode, storagePlace);
        } catch (SQLException e) {
            if(e.getSQLState().equals("23503")){
                throw new ConstraintViolationException("Errore: L'Item con itemCode [" + itemCode + "] non può essere eliminato perché sono ancora presenti Copie/Prenotazioni/Prestiti.");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void updatePhysicalCopies(int itemCode, String storagePlace, int newNumberOfCopies, boolean borrowable)
            throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        try {
            String query = """
                     UPDATE physical_copies 
                     SET number_of_copies = ?, borrowable = ? 
                     WHERE code = ? AND storage_place = ?; 
                     """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newNumberOfCopies);
            ps.setBoolean(2, borrowable);
            ps.setInt(3, itemCode);
            ps.setString(4, storagePlace);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Item con ItemCode [" + itemCode + "] non ha copie nella sede [" + storagePlace + "].");
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23503")){
                throw new ConstraintViolationException("Errore: Non puoi avere 0 copie di un Item.");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public HashMap<Library, PhysicalCopies> getPhysicalCopies(int itemCode) throws DatabaseConnectionException {
        try {
            String query = """
                    SELECT * 
                    FROM physical_copies P 
                    WHERE P.code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ResultSet copiesSet = ps.executeQuery();
            HashMap<Library, PhysicalCopies> physicalCopies = new HashMap<>();
            while (copiesSet.next()) {
                physicalCopies.put(
                        Library.valueOf(copiesSet.getString("storage_place")),
                        new PhysicalCopies(
                                copiesSet.getInt("number_of_copies"),
                                copiesSet.getInt("number_of_available_copies"),
                                copiesSet.getBoolean("borrowable")));
            }
            return physicalCopies;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}
