package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

public class MagazineDAO {

    private Connection connection;

    public MagazineDAO() {
        this.connection = ConnectionManager.getInstance().getConnection();
    }

    public Magazine getMagazine(int itemCode) throws IdNotFoundException, DatabaseConnectionException {
        try {
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
            return magazine;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public int addMagazine(String title,
                           String publicationDate,
                           String language,
                           String category,
                           String link,
                           String publishingHouse,
                           int number_of_pages)
            throws DatabaseConnectionException {
        try {
            //Creazione Item e Magazine
            String query = """
                    INSERT INTO Item (title, publication_date, language, category, link, number_of_pages) 
                    VALUES (?, ?, ?, ?, ?, ?) 
                    RETURNING code; 
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, number_of_pages);

            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            int itemCode = resultSet.getInt("code");
            
            String query_2 = """
                    INSERT INTO Magazine (code, publishing_house) 
                    VALUES (?, ?);
                    """;
            ps = connection.prepareStatement(query_2);
            ps.setInt(1, itemCode);
            ps.setString(2, publishingHouse);
            ps.executeUpdate();
            return itemCode;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void removeMagazine(int itemCode) throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        try {
            String query = """
                    DELETE FROM Magazine 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Il Magazine con itemCode [" + itemCode + "] non è presente nel DB.");
            }

            query = """
                DELETE FROM Item 
                WHERE code = ?;
                """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: L'Item che vuoi rimuovere non è un Magazine. [problema su chiamata del metodo]");
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23503")){
                throw new ConstraintViolationException("Errore: Magazine con itemCode [" + itemCode + "] non può essere eliminato perché sono ancora presenti Copie/Prenotazioni/Prestiti. [problema del programma controllare logica di cancellazione elemento]");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void updateMagazine(int originalItemCode,
                               String title,
                               String publicationDate,
                               String language,
                               String category,
                               String link,
                               String publishingHouse,
                               int numberOfPages)
            throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        try {
            String query = """
                    UPDATE Item 
                    SET title = ?, publication_date = ?, language = ?, category = ?, link = ?, number_of_pages = ? 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, numberOfPages);
            ps.setInt(7, originalItemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Magazine con ItemCode [" + originalItemCode + "] non presente nel DB.");
            }

            query = """
                    UPDATE Magazine 
                    SET publishing_house = ? 
                    WHERE code = ?;
                    """;
            ps = connection.prepareStatement(query);
            ps.setString(1, publishingHouse);
            ps.setInt(2, originalItemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Item con ItemCode [" + originalItemCode + "] non è un Magazine.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public ArrayList<Magazine> getAllMagazines() throws DatabaseConnectionException {
        ArrayList<Magazine> magazines = new ArrayList<>();
        try {
            String query = """
                    SELECT * 
                    FROM Item I JOIN Magazine M ON I.code = M.code;
                    """;
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
            return magazines;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}