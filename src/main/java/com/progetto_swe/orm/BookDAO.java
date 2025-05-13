package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;

public class BookDAO {

    private Connection connection;

    public BookDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Book getBook(int itemCode) throws IdNotFoundException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
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
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            book.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(itemCode));
            return book;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
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
                       String authors,
                       String storagePlace,
                       int numberOfCopies,
                       boolean borrowable)
            throws IdAlreadyExistsException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();
        try {
            //Creazione Item e Book
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
            ps.setInt(6, numberOfPages);

            ResultSet resultSet = ps.executeQuery();
            int itemCode = resultSet.getInt("code");

            query = """
                    INSERT INTO Book (code, isbn, publishing_house, authors) 
                    VALUES (?, ?, ?, ?);
                    """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);
            ps.setString(2, isbn);
            ps.setString(3, publishingHouse);
            ps.setString(4, authors);
            ps.executeUpdate();

            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.addPhysicalCopies(itemCode, storagePlace, numberOfCopies, borrowable);
            ConnectionManager.commit();
            return itemCode;
        } catch (SQLException e) {
            ConnectionManager.rollback();
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void removeBook(int itemCode) throws IdNotFoundException, ConstrainViolationException, DatabaseConnectionException {
        connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();
        try {
            String query = """
                    DELETE FROM Book 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Il Book con itemCode [" + itemCode + "] non è presente nel DB.");
            }

            query = """
                    DELETE FROM Item 
                    WHERE code = ?;
                    """;
            ps = connection.prepareStatement(query);
            ps.setInt(1, itemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: L'Item con ItemCode [" + itemCode + "] che vuoi rimuovere non è un Book. " +
                        "[SEI UN COGLIONE]");
            }
            ConnectionManager.commit();
        } catch (SQLException e) {
            ConnectionManager.rollback();
            if(e.getSQLState().equals("23503")){
                throw new ConstrainViolationException("Errore: Book con itemCode [" + itemCode + "] non può essere eliminato " +
                        "perché sono ancora presenti Copie/Prenotazioni/Prestiti. " +
                        "[SEI UN COGLIONE]");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
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
                           String storagePlace,
                           int newNumberOfCopies,
                           boolean borrowable)
            throws IdNotFoundException, ConstrainViolationException, DatabaseConnectionException{
        connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();
        try {
            String query = """
                    UPDATE Item 
                    SET title = ?, publication_date = ?, language = ?, category = ?, link = ? 
                    WHERE code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, originalItemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Book con ItemCode [" + originalItemCode + "] non presente nel DB.");
            }

            query = "UPDATE Book SET isbn = ?, publishing_house = ?, authors = ? WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            ps.setString(2, publishingHouse);
            ps.setString(3, authors);
            ps.setInt(4, originalItemCode);

            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Item con ItemCode [" + originalItemCode + "] che cuoi aggiornare non è un Book.");
            }
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            physicalCopiesDAO.updatePhysicalCopies(originalItemCode, storagePlace, newNumberOfCopies, borrowable);
            ConnectionManager.commit();
        } catch (SQLException e) {
            ConnectionManager.rollback();
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }


    public ArrayList<Book> getAllBooks() throws DatabaseConnectionException {
        ArrayList<Book> books = new ArrayList<>();
        connection = ConnectionManager.getConnection();
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

            for(Book book : books){
                PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
                book.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(book.getCode()));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

}