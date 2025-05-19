package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

public class ThesisDAO {

    private Connection connection;

    public ThesisDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Thesis getThesis(int itemCode) throws IdNotFoundException, DatabaseConnectionException{
        connection = ConnectionManager.getConnection();
        try {
            String query = """
                    SELECT * 
                    FROM Item I JOIN Thesis T ON I.code = T.code 
                    WHERE I.code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Thesis con itemCode [" + itemCode + "] non presente nel DB.");
            }

            Thesis thesis = new Thesis(
                    itemCode,
                    resultSet.getString("title"),
                    LocalDate.parse(resultSet.getString("publication_date")),
                    Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")),
                    resultSet.getString("link"),
                    resultSet.getInt("number_of_pages"),
                    resultSet.getString("author"),
                    resultSet.getString("supervisors"),
                    resultSet.getString("university"));
            return thesis;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public int addThesis(String title,
                         String publicationDate,
                         String language,
                         String category,
                         String link,
                         int number_of_pages,
                         String author,
                         String supervisors,
                         String university)
            throws DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Thesis
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

            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            resultSet.next();

            int itemCode = resultSet.getInt(1);;

            String query_2 = """
                    INSERT INTO Thesis (code, author, supervisors, university) 
                    VALUES (?, ?, ?, ?);
                    """;
            ps = connection.prepareStatement(query_2);
            ps.setInt(1, itemCode);
            ps.setString(2, author);
            ps.setString(3, supervisors);
            ps.setString(4, university);
            ps.executeUpdate();

            return itemCode;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void removeThesis(int itemCode) throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();

        try {
            String query = """
                    DELETE FROM Thesis 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Il Thesis con itemCode [" + itemCode + "] non è presente nel DB.");
            }

            String query_2 = """
                DELETE FROM Item 
                WHERE code = ?;
                """;
            ps = connection.prepareStatement(query_2);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: L'Item che vuoi rimuovere non è un Thesis. [problema su chiamata del metodo]");
            }
            ConnectionManager.commit();
        } catch (SQLException e) {
            ConnectionManager.rollback();
            if(e.getSQLState().equals("23503")){
                throw new ConstraintViolationException("Errore: Thesis con itemCode [" + itemCode + "] non può essere eliminato perché sono ancora presenti Copie/Prenotazioni/Prestiti. [problema del programma controllare logica di cancellazione elemento]");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void updateThesis(int originalItemCode,
                             String title,
                             String publicationDate,
                             String language,
                             String category,
                             String link,
                             String author,
                             String supervisors,
                             String university,
                             int numberOfPages)
            throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        try {
            String query = """
                        UPDATE Item SET title = ?, publication_date = ?, language = ?, category = ?, link = ?, number_of_pages = ? WHERE code = ?;
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
                throw new IdNotFoundException("Errore: Thesis con ItemCode [" + originalItemCode + "] non presente nel DB.");
            }

            query = """
                    UPDATE Thesis 
                    SET author = ?, supervisors = ?, university = ? 
                    WHERE code = ?;
                    """;
            ps = connection.prepareStatement(query);
            ps.setString(1, author);
            ps.setString(2, supervisors);
            ps.setString(3, university);
            ps.setInt(4, originalItemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Item con ItemCode [" + originalItemCode + "] non è un Thesis.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public ArrayList<Thesis> getAllThesis() throws DatabaseConnectionException {
        ArrayList<Thesis> thesis = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query = """
                    SELECT * 
                    FROM Item I JOIN Thesis T ON 
                    I.code = T.code;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                thesis.add(new Thesis(
                        resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")),
                        resultSet.getString("link"),
                        resultSet.getInt("number_of_pages"),
                        resultSet.getString("author"),
                        resultSet.getString("supervisors"),
                        resultSet.getString("university")));
            }
            return thesis;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }
}
