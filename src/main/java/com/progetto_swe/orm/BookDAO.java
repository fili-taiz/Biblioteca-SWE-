package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.Book;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Library;

public class BookDAO {

    private Connection connection;

    public BookDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Book getBook(String itemId) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category, String link, String isbn,
            String publishingHouse, int numberOfPages, String authors, String storagePlace, int numberOfCopies) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsBook(statement, originalItemCode)) {
                return false;
            }
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', borrowable = " + borrowable
                    + ", language = '" + language + "', category = '" + category + "', link = '" + link + "'"
                    + "WHERE code = '" + originalItemCode + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            query
                    = "UPDATE Book "
                    + "SET isbn = '" + isbn + "', publishing_house = '" + publishingHouse + "', number_of_pages = " + numberOfPages
                    + ", authors = '" + authors + "' "
                    + "WHERE code = '" + originalItemCode + "';";
            statement.executeQuery(query);
            resultSet.next();

            if (containsCopies(statement, numberOfCopies, storagePlace) <= -1) {
                return true;
            }

            query
                    = "UPDATE Physical_copies "
                    + "SET number_of_copies = " + numberOfCopies
                    + "WHERE code = '" + originalItemCode + "' AND storage_place = '" + storagePlace + "'";
            resultSet = statement.executeQuery(query);
            resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    private boolean containsBook(Statement statement, int code) {
        try {
            String query
                    = "SELECT *"
                    + "FROM (SELECT * FROM Item WHERE I.code = " + code + ") I JOIN Book B ON I.code = B.code ";
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
        }
        return false;
    }

    private int containsBook(Statement statement, String title, String publicationDate, String borrowable, String language, String category, String link,
            String isbn, String publishingHouse, int numberOfPages, String authors) {

        try {
            String query
                    = "SELECT code"
                    + "FROM Item I JOIN Book B ON I.code = B.code"
                    + "WHERE I.title = '" + title + "' AND I.publicationDate = '" + publicationDate + "' AND I.borrowable = " + borrowable
                    + " AND I.language = '" + language + "' AND I.category = '" + category + "' AND B.link = '" + link + "'"
                    + " AND B.isbn = '" + isbn + "' AND B.publishing_house = '" + publishingHouse + "' AND B.number_of_pages = '" + numberOfPages + "' "
                    + " AND B.authors = '" + authors + "';";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("code");
            }
        } catch (SQLException e) {
        }
        return -1;
    }

    private int containsCopies(Statement statement, int code, String storagePlace) {
        try {
            String query
                    = "SELECT P.number_of_copies"
                    + "FROM Phisical_copies P"
                    + "WHERE P.code = '" + code + "' AND i.storage_place = '" + storagePlace + "'; ";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                resultSet.getInt("number_of_copies");
            }
        } catch (SQLException e) {
        }
        return -1;
    }


    /*
     * Aggiunta libro
     * 2. controllo esistenza del libro
     * 3. controllo esistenza libro nella biblioteca
     * 4. aggiunta
     * 
     * 
     * 1. non c'e libro -> aggiungo item libro e creo copies
     * 2. c'e il libro ma non c'è copies -> creo copied
     * 3. c'è il libro c'è il copied -> update copies
     * 
     */
    public int addBook(String title, String publicationDate, String borrowable, String language, String category, String link, String isbn,
            String publishingHouse, int numberOfPages, String authors, String storagePlace, int numberOfCopies) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            int code = containsBook(statement, title, publicationDate, borrowable, language, category, link, isbn, publishingHouse, numberOfPages, authors);
            int copiesStored = containsCopies(statement, code, storagePlace);

            //libro non presente
            if (code <= -1) {
                //Creazione Item e Book
                String query = "INSERT INTO Item (title, publication_date, borrowable, language, category, link)"
                        + "VALUES ('" + title + "', '" + publicationDate + "', " + borrowable + ", '" + language + "', '" + category + "', '" + link + "') "
                        + "RETURNING code;";
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    return -1;
                }
                code = resultSet.getInt("code");
                query = "INSERT INTO Book (code, isbn, publishing_house, number_of_pages, authors)"
                        + "VALUES ('" + code + "', '" + isbn + "', " + publishingHouse + ", " + numberOfPages + ", '" + authors + "');";
                statement.executeQuery(query);
            }

            //Copia non presente
            if (copiesStored <= -1) {
                //Creazione copia
                String query = "INSERT INTO Physical_copies (code, storage_place, number_of_copies)"
                        + "VALUES ('" + code + "', '" + storagePlace + "', " + numberOfCopies + "');";
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    code = resultSet.getInt("code");
                    return code;
                } else {
                    return -1;
                }
            }

            //Copia presente
            if (code > -1 && copiesStored > -1) {
                String query
                        = "UPDATE Physical_copies "
                        + "SET number_of_copies = " + (copiesStored + numberOfCopies)
                        + "WHERE code = '" + code + "' AND storage_place = '" + storagePlace + "'; ";
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                return code;
            }
        } catch (SQLException e) {
        }
        return -1;
    }

    public boolean removeBook(int code, String storagePlace) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsBook(statement, code)) {
                return false;
            }

            if (containsCopies(statement, code, storagePlace) <= -1) {
                return false;
            }

            String query = "DELETE FROM Phisycal_copies "
                    + "WHERE code = " + code + ";";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return false;
            }

            query
                    = "SELECT P.number_of_copies"
                    + "FROM Phisical_copies P"
                    + "WHERE P.code = '" + code + "'; ";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return true;
            }

            query = "DELETE FROM Book "
                    + "WHERE code = " + code + ";";
            statement.executeQuery(query);
            query = "DELETE FROM Item "
                    + "WHERE code = " + code + ";";
            statement.executeQuery(query);
            return true;

        } catch (SQLException e) {

        }
        return false;
    }

    public ArrayList<Item> getAllBook() {
        ArrayList<Item> result = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Book B ON I.code = B.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Item i = new Book(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getBoolean("borrowable"), resultSet.getString("isbn"),
                        resultSet.getString("publishing_house"), resultSet.getInt("number_of_page"), resultSet.getString("authors"));
                query
                        = "SELECT * "
                        + "FROM Physical_copies P "
                        + "WHERE P.code = " + resultSet.getInt("code");
                ResultSet copiesSet = statement.executeQuery(query);
                while (copiesSet.next()) {
                    i.addCopies(Library.valueOf(copiesSet.getString("storage_place")), copiesSet.getInt("number_of_copies"));
                }
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
    }
}
