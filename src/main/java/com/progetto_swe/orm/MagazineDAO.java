package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

public class MagazineDAO {

    private Connection connection;

    public MagazineDAO() {
        this.connection = ConnectionManager.getConnection();
    }
//TODO stesse modifiche di eccezioni e paramtri di BookDAO
    public Magazine getMagazine(int itemCode) throws IdNotFoundException, DatabaseConnectionException {
        try {
            connection = ConnectionManager.getConnection();
            String query = """
                    SELECT * 
                    FROM Item I JOIN Magazine M ON I.code = M.code 
                    WHERE I.code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Magazine con itemCode [" + itemCode + "] non presente nel DB.");
            }

            Magazine magazine = new Magazine(
                    itemCode,
                    resultSet.getString("title"),
                    LocalDate.parse(resultSet.getString("publication_date")),
                    Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")),
                    resultSet.getString("link"),
                    resultSet.getInt("number_of_pages"),
                    resultSet.getString("publishing_house"));
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            magazine.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(itemCode));
            return magazine;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public int addMagazine(String title,
                           String publicationDate,
                           String language,
                           String category,
                           String link,
                           String publishingHouse,
                           int number_of_pages,
                           String storagePlace,
                           int numberOfCopies,
                           boolean borrowable)
            throws IdAlreadyExistsException, DatabaseConnectionException {

        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Magazine
            String query = """
                    INSERT INTO Item (title, publication_date, language, category, link, number_of_pages) 
                    VALUES (?, ?, ?, ?, ?, ?) 
                    RETURNING code; 
                    """;
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, number_of_pages);

            ResultSet resultSet = ps.executeQuery();
            int itemCode = resultSet.getInt("code");

            query = """
                    INSERT INTO Magazine (code, publishing_house) 
                    VALUES (?, ?);
                    """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, publishingHouse);
            ps.executeUpdate();

            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(itemCode, storagePlace, numberOfCopies, borrowable);
            return itemCode;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void removeMagazine(int itemCode) throws IdNotFoundException, ConstrainViolationException, DatabaseConnectionException {

        connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();
        try {
            String query = "DELETE FROM Magazine WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Il Magazine con itemCode [" + itemCode + "] non è presente nel DB.");
            }

            query = "DELETE FROM Item WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: L'Item che vuoi rimuovere non è un Magazine. [problema su chiamata del metodo]");
            }
            ConnectionManager.commit();
        } catch (SQLException e) {
            ConnectionManager.rollback();
            if(e.getSQLState().equals("23503")){
                throw new ConstrainViolationException("Errore: Magazine con itemCode [" + itemCode + "] non può essere eliminato perché sono ancora presenti Copie/Prenotazioni/Prestiti. [problema del programma controllare logica di cancellazione elemento]");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category,
                               String link, String publishingHouse, String storagePlace, int newNumberOfCopies, boolean borrowable) throws IdNotFoundException, ConstrainViolationException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "UPDATE Item SET title = ?, publication_date = ?, language = ?, category = ?, link = ? WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, originalItemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Magazine con ItemCode [" + originalItemCode + "] non presente nel DB.");
            }

            query = "UPDATE Magazine SET publishing_house = ? WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, publishingHouse);
            ps.setInt(2, originalItemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Item con ItemCode [" + originalItemCode + "] non è un Magazine.");
            }
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.updatePhysicalCopies(originalItemCode, storagePlace, newNumberOfCopies, borrowable);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public ArrayList<Magazine> getAllMagazines() throws DatabaseConnectionException {
        ArrayList<Magazine> magazines = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            //tutti i magazine
            String query
                    = "SELECT * FROM Item I JOIN Magazine M ON I.code = M.code";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                magazines.add(new Magazine(resultSet.getInt("code"),
                        resultSet.getString("title"),
                        LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")),
                        resultSet.getString("link"),
                        resultSet.getInt("number_of_pages"),
                        resultSet.getString("publishing_house")));
            }

            for(Magazine magazine : magazines){
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                magazine.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(magazine.getCode()));
            }
            return magazines;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }
}