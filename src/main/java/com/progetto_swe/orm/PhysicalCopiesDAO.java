package com.progetto_swe.orm;


import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PhysicalCopiesDAO {
    private Connection connection;
//TODO Guarda BookDAO
    public PhysicalCopiesDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public boolean addPhysicalCopies(int itemCode,
                                     String storagePlace,
                                     int numberOfCopies,
                                     boolean borrowable)
            throws IdAlreadyExistsException {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO physical_copies (code, storage_place, number_of_copies, borrowable, nuber_of_available_copies) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);
            ps.setInt(3, numberOfCopies);
            ps.setBoolean(4, borrowable);
            ps.setInt(5, numberOfCopies);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Articolo con userCode [" + itemCode + "] già presente nella sede [" + storagePlace + "].");
            }
            System.out.println("SQLException: " + e.getMessage());
            return false;//TODO eccezione generica
        }
    }

    public void removePhysicalCopies(int itemCode, String storagePlace) throws IdNotFoundException, ConstrainViolationException, DatabaseConnectionException {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM physical_copies P WHERE code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, storagePlace);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: L'Item con itemCode [" + itemCode + "] non ha copie presso la sede [" + storagePlace + "].");
            }

            WaitingListDAO waitingListDAO = new WaitingListDAO();
            waitingListDAO.removeWaitingList(itemCode, storagePlace);
        } catch (SQLException e) {//TODO aggiungere controllo che per la rimozione di un articolo il numero di available e total copies deve combaciare
            ConnectionManager.rollback();
            if(e.getSQLState().equals("23503")){
                throw new ConstrainViolationException("Errore: L'Item con itemCode [" + itemCode + "] non può essere eliminato perché sono ancora presenti Copie/Prenotazioni/Prestiti. [problema del programma controllare logica di cancellazione elemento]");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void updatePhysicalCopies(int itemCode, String storagePlace, int newNumberOfCopies, boolean borrowable) throws IdNotFoundException, DatabaseConnectionException {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "UPDATE physical_copies SET number_of_copies = ?, borrowable = ? WHERE code = ? AND storage_place = ?; ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newNumberOfCopies);
            ps.setBoolean(2, borrowable);
            ps.setInt(3, itemCode);
            ps.setString(4, storagePlace);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Item con ItemCode [" + itemCode + "] non ha copie nella sede [" + storagePlace + "].");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public HashMap<Library, PhysicalCopies> getPhysicalCopies(int code) throws DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM physical_copies P WHERE P.code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet copiesSet = ps.executeQuery();
            HashMap<Library, PhysicalCopies> physicalCopies = new HashMap<>();
            while (copiesSet.next()) {
                physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getInt("number_of_available_copies"), copiesSet.getBoolean("borrowable")));
            }
            return physicalCopies;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }
}
