package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;

public class BookDAO {
/*
    private Connection connection;

    public BookDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public int addBook(String title, String publicationDate, String language, String category, String link, String isbn,
                       String publishingHouse, int numberOfPages, String authors) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            //Creazione Item e Book
            String query
                    = "INSERT INTO Item (title, publication_date, language, category, link)"
                    + "VALUES ('" + title + "', '" + publicationDate + "', '" + language + "', '" + category + "', '" + link + "') "
                    + "RETURNING code;";
            ResultSet resultSet = statement.executeQuery(query);
            int code = resultSet.getInt("code");

            query
                    = "INSERT INTO Book (code, isbn, publishing_house, number_of_pages, authors)"
                    + "VALUES ('" + code + "', '" + isbn + "', " + publishingHouse + ", " + numberOfPages + ", '" + authors + "');";
            statement.executeUpdate(query); //si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE

            return code;
        } catch (SQLException e) {
        }
        return -1;
    }

    public boolean removeBook(int code) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            //Creazione Item e Book
            String query
                    = "DELETE FROM Item "
                    + "WHERE code = '" + code + "';";
            statement.executeUpdate(query);

            query
                    = "DELETE FROM Book "
                    + "where code = '" + code + "';";
            statement.executeUpdate(query); //si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Book getBook(String itemId) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String language, String category, String link, String isbn,
                              String publishingHouse, int numberOfPages, String authors) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', language = '" + language + "', category = '" + category + "', link = '" + link + "'"
                    + "WHERE code = '" + originalItemCode + "';";
            statement.executeUpdate(query);

            query
                    = "UPDATE Book "
                    + "SET isbn = '" + isbn + "', publishing_house = '" + publishingHouse + "', number_of_pages = " + numberOfPages
                    + ", authors = '" + authors + "' "
                    + "WHERE code = '" + originalItemCode + "';";
            statement.executeQuery(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }


    public ArrayList<Item> getAllBook() {
        ArrayList<Item> result = new ArrayList<>();
        HashMap<Library, PhysicalCopies> physicalCopies;
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Book B ON I.code = B.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Item i = new Book(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getString("isbn"),
                        resultSet.getString("publishing_house"), resultSet.getInt("number_of_page"), resultSet.getString("authors"));
                query
                        = "SELECT * "
                        + "FROM Physical_copies P "
                        + "WHERE P.code = " + resultSet.getInt("code");
                ResultSet copiesSet = statement.executeQuery(query);
                physicalCopies = new HashMap<>();
                while (copiesSet.next()) {
                    physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getBoolean("borrowable")));
                }
                i.setPhysicalCopies(physicalCopies);
                result.add(i);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return result;
    }


    public HashMap<Library, Integer> getBookCopies(Statement statement, int code) {
        HashMap<Library, Integer> copies = new HashMap<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Physical_copies P "
                    + "WHERE P.code = " + code;
            ResultSet copiesSet = statement.executeQuery(query);
            while (copiesSet.next()) {
                copies.put(Library.valueOf(copiesSet.getString("storage_place")), copiesSet.getInt("number_of_copies"));
            }
            return copies;
        } catch (SQLException e) {
        }
        return copies;
    }

    public HashMap<Library, Integer> getBookLendings(Statement statement, int code) {
        HashMap<Library, Integer> copies = new HashMap<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Physical_copies P "
                    + "WHERE P.code = " + code;
            ResultSet copiesSet = statement.executeQuery(query);
            while (copiesSet.next()) {
                copies.put(Library.valueOf(copiesSet.getString("storage_place")), copiesSet.getInt("number_of_copies"));
            }
            return copies;
        } catch (SQLException e) {
        }
        return copies;
    }*/
}