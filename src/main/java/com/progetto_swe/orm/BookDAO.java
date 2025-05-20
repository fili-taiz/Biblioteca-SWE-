package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

public class BookDAO {

    private Connection connection;

    public BookDAO() {
        this.connection = ConnectionManager.getInstance().getConnection();
    }

    public Book getBook(int itemCode) throws IdNotFoundException, DatabaseConnectionException {
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                    SELECT * 
                    FROM Item I JOIN Book B ON I.code = B.code 
                    WHERE I.code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Book con itemCode [" + itemCode + "] non presente nel DB.");
            }

            Book book = new Book(
                    itemCode,
                    resultSet.getString("title"),
                    LocalDate.parse(resultSet.getString("publication_date")),
                    Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")),
                    resultSet.getString("link"),
                    resultSet.getString("isbn"),
                    resultSet.getString("publishing_house"),
                    resultSet.getInt("number_of_pages"),
                    resultSet.getString("authors"));
            return book;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public int addBook(String title,
                       String publicationDate,
                       String language,
                       String category,
                       String link,
                       String isbn,
                       String publishingHouse,
                       int numberOfPages,
                       String authors)
            throws DatabaseConnectionException {
        connection = ConnectionManager.getInstance().getConnection();
        try {
            //Creazione Item e Book
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
            ps.setInt(6, numberOfPages);

            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            int itemCode = resultSet.getInt("code");

            String query_2 = """
                    INSERT INTO Book (code, isbn, publishing_house, authors) 
                    VALUES (?, ?, ?, ?);
                    """;
            PreparedStatement ps2 = connection.prepareStatement(query_2);
            ps2.setInt(1, itemCode);
            ps2.setString(2, isbn);
            ps2.setString(3, publishingHouse);
            ps2.setString(4, authors);
            ps2.executeUpdate();

            return itemCode;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    public void removeBook(int itemCode) throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException {
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                    DELETE FROM Book 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Il Book con itemCode [" + itemCode + "] non è presente nel DB.");
            }

            String query_2 = """
                    DELETE FROM Item 
                    WHERE code = ?;
                    """;
            ps = connection.prepareStatement(query_2);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: L'Item con ItemCode [" + itemCode + "] che vuoi rimuovere non è un Book. ");
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("23503")){
                throw new ConstraintViolationException("Errore: Book con itemCode [" + itemCode + "] non può essere eliminato " +
                        "perché sono ancora presenti Copie/Prenotazioni/Prestiti. ");
            }
            throw new DatabaseConnectionException(e);
        }
    }

    public void updateBook(int originalItemCode,
                           String title,
                           String publicationDate,
                           String language,
                           String category,
                           String link,
                           String isbn,
                           String publishingHouse,
                           String authors,
                           int numberOfPages)
            throws IdNotFoundException, ConstraintViolationException, DatabaseConnectionException{
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                    UPDATE Item 
                    SET title = ?, publication_date = ?, language = ?, category = ?, link = ? , number_of_pages = ?
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
                throw new IdNotFoundException("Errore: Book con ItemCode [" + originalItemCode + "] non presente nel DB.");
            }

            query = """
                    UPDATE Book 
                    SET isbn = ?, publishing_house = ?, authors = ? 
                    WHERE code = ?;
                    """;
            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            ps.setString(2, publishingHouse);
            ps.setString(3, authors);
            ps.setInt(4, originalItemCode);

            if(ps.executeUpdate() != 1) {
                throw new IdNotFoundException("Errore: Item con ItemCode [" + originalItemCode + "] che cuoi aggiornare non è un Book.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }


    public ArrayList<Book> getAllBooks() throws DatabaseConnectionException {
        ArrayList<Book> books = new ArrayList<>();
        connection = ConnectionManager.getInstance().getConnection();
        try {
            String query = """
                    SELECT * 
                    FROM Item I JOIN Book B ON I.code = B.code;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("code"),
                        resultSet.getString("title"),
                        LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")),
                        resultSet.getString("link"),
                        resultSet.getString("isbn"),
                        resultSet.getString("publishing_house"),
                        resultSet.getInt("number_of_pages"),
                        resultSet.getString("authors")));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e);
        }
    }

}