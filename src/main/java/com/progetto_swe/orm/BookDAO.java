package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class BookDAO {

    private Connection connection;

    public BookDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Book getBook(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Book B ON I.code = B.code"
                    + "WHERE I.code = " + code + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }
            Book book = new Book(code, resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getString("isbn"),
                    resultSet.getString("publishing_house"), resultSet.getInt("number_of_page"), resultSet.getString("authors"));
            query
                    = "SELECT * "
                    + "FROM Physical_copies P "
                    + "WHERE P.code = " + code + ";";
            ResultSet copiesSet = statement.executeQuery(query);
            if(copiesSet.next()){
                HashMap<Library, PhysicalCopies> physicalCopies = new HashMap<>();
                while (copiesSet.next()) {
                    physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getBoolean("borrowable")));
                }
                book.setPhysicalCopies(physicalCopies);
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
            return book;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public int addBook(String title, String publicationDate, String language, String category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            //Creazione Item e Book
            String query
                    = "INSERT INTO Item (title, publication_date, language, category, link)"
                    + "VALUES ('" + title + "', '" + publicationDate + "', '" + language + "', '" + category + "', '" + link + "') "
                    + "RETURNING code;";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing insert!", null);
            }
            int code = resultSet.getInt("code");

            query
                    = "INSERT INTO Book (code, isbn, publishing_house, number_of_pages, authors)"
                    + "VALUES ('" + code + "', '" + isbn + "', " + publishingHouse + ", " + numberOfPages + ", '" + authors + "');";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }

            return code;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeBook(int code) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            String query
                    = "DELETE FROM Item "
                    + "WHERE code = '" + code + "';";
            if (statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            };

            query
                    = "DELETE FROM Book "
                    + "where code = '" + code + "';";

            if (statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String language, String category, String link, String isbn,
                              String publishingHouse, int numberOfPages, String authors) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            //TODO guarda se ho controllato che questo libro sia dentro al catalogue;
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', language = '" + language + "', category = '" + category + "', link = '" + link + "'"
                    + "WHERE code = '" + originalItemCode + "';";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }

            query
                    = "UPDATE Book "
                    + "SET isbn = '" + isbn + "', publishing_house = '" + publishingHouse + "', number_of_pages = " + numberOfPages + ", authors = '" + authors + "' "
                    + "WHERE code = '" + originalItemCode + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}