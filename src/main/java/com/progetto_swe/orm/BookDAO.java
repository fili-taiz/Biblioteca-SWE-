package com.progetto_swe.orm;

import java.sql.*;
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
                    = "SELECT * FROM Item I JOIN Book B ON I.code = B.code WHERE I.code = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
                //System.out.println("There is no book in the database with code = " + code + "!");
                return null;
            }
            Book book = new Book(code, resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getString("isbn"),
                    resultSet.getString("publishing_house"), resultSet.getInt("number_of_pages"), resultSet.getString("authors"));
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            book.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(code));
            return book;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public int addBook(String title, String publicationDate, String language, String category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {

        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Book
            String query = "INSERT INTO Item (title, publication_date, language, category, link, number_of_pages) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, numberOfPages);


            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new CRUD_exception("Error executing insert!", null);
            }
            int code = generatedKeys.getInt(1);

            query
                    = "INSERT INTO Book (code, isbn, publishing_house, authors)"
                    + " VALUES (?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, isbn);
            ps.setString(3, publishingHouse);
            ps.setString(4, authors);

            ps.executeUpdate();

            return code;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return -1;
        }
    }

    public boolean removeBook(int code) {

        connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Book WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.executeUpdate();

            query = "DELETE FROM Item WHERE code = ?;";


            ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String language, String category, String link, String isbn,
                              String publishingHouse, String authors) {
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

            if(ps.executeUpdate() == 0){
                return false;
            }

            query = "UPDATE Book SET isbn = ?, publishing_house = ?, authors = ? WHERE code = ?;";

            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            ps.setString(2, publishingHouse);
            ps.setString(3, authors);
            ps.setInt(4, originalItemCode);

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

}