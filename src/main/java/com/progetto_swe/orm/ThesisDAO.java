package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class ThesisDAO {

    private Connection connection;

    public ThesisDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public int addThesis(String title, String publicationDate, String language, String category, String link, String author, String supervisors, String university) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            //Creazione Item e Thesis
            String query
                    = "INSERT INTO Item (title, publication_date, borrowable, language, category, link)"
                    + "VALUES ('" + title + "', '" + publicationDate + "', '" + language + "', '" + category + "', '" + link + "') "
                    + "RETURNING code;";
            ResultSet resultSet = statement.executeQuery(query);
            int code = resultSet.getInt("code");

            query
                    = "INSERT INTO Thesis (code, author, supervisors, university)"
                    + "VALUES ('" + code + "', '" + author + "', '" + supervisors + "', '" + university + "');";
            statement.executeUpdate(query); //si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE

            return code;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeThesis(int code) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            //Creazione Item e Book
            String query
                    = "DELETE FROM Item "
                    + "WHERE code = '" + code + "';";
            statement.executeUpdate(query);

            query
                    = "DELETE FROM Thesis "
                    + "where code = '" + code + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }

            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public Thesis getThesis(String itemId) {
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return null;
    }

    public boolean updateThesis(int originalItemCode,String title, String publicationDate, String language, String category, String link, String author,
                                String supervisors, String university) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsThesis(statement, originalItemCode)) {
                return false;
            }
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', language = '" + language + "', category = '" + category + "', link = '" + link + "';";
            statement.executeUpdate(query);

            query
                    = "UPDATE Thesis "
                    + "SET author = '" + author + "', supervisors = '" + supervisors + "', university = '" + university + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    private boolean containsThesis(Statement statement, int code) {
        try {
            String query
                    = "SELECT *"
                    + "FROM (SELECT * FROM Item WHERE I.code = " + code + ") I JOIN Thesis T ON I.code = T.code; ";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error", null);
        }
    }

    private int containsThesis(Statement statement, String title, String publicationDate, String borrowable, String language, String category, String link,
                               String author, String supervisors, String university) {

        try {
            String query
                    = "SELECT code"
                    + "FROM Item I JOIN Thesis T ON I.code = T.code"
                    + "WHERE I.title = '" + title + "' AND I.publicationDate = '" + publicationDate + "' AND I.borrowable = " + borrowable
                    + " AND I.language = '" + language + "' AND I.category = '" + category + "' AND B.link = '" + link + "'"
                    + " AND T.author = '" + author + "' AND T.supervisors = '" + supervisors + "' AND university = '" + university + "';";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("code");
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error", null);
        }
    }

    private int containsCopies(Statement statement, int code, String storagePlace) {
        try {
            String query
                    = "SELECT P.number_of_copies"
                    + "FROM Physical_copies P"
                    + "WHERE P.code = '" + code + "' AND i.storage_place = '" + storagePlace + "'; ";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                resultSet.getInt("number_of_copies");
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return -1;
    }

    public int addThesis(String title, String publicationDate, String borrowable, String language, String category, String link, String author,
                         String supervisors, String university, String storagePlace, int numberOfCopies) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            int code = containsThesis(statement, title, publicationDate, borrowable, language, category, link, author, supervisors, university);
            int copiesStored = containsCopies(statement, code, storagePlace);

            //libro non presente
            if (code <= -1) {
                //Creazione Item e Thesis
                String query = "INSERT INTO Item (title, publication_date, borrowable, language, category, link)"
                        + "VALUES ('" + title + "', '" + publicationDate + "', " + borrowable + ", '" + language + "', '" + category + "', '" + link + "') "
                        + "RETURNING code;";
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    return -1;
                }
                code = resultSet.getInt("code");
                query = "INSERT INTO Thesis (code, author, supervisors, university)"
                        + "VALUES ('" + code + "', '" + author + "', " + supervisors + ", " + university + "');";
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
                if(!resultSet.next()){
                    throw new CRUD_exception("Error executing query!", null);
                }
                return code;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", null);
        }
        return -1;
    }

    public ArrayList<Item> getAllThesis() {
        ArrayList<Item> result = new ArrayList<>();
        HashMap<Library,PhysicalCopies> physicalCopies;
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Thesis T ON I.code = T.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            while (resultSet.next()) {
                Item i = new Thesis(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")), Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getString("author"),
                        resultSet.getString("supervisors"), resultSet.getString("university"));
                query
                        = "SELECT * "
                        + "FROM Physical_copies P "
                        + "WHERE P.code = " + resultSet.getInt("code");
                ResultSet copiesSet = statement.executeQuery(query);
                if(!copiesSet.next()){
                    throw new DataAccessException("Error executing query!", null);
                }
                physicalCopies = new HashMap<>();
                while (copiesSet.next()) {
                    physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getBoolean("borrowable")));
                }
                i.setPhysicalCopies(physicalCopies);
                result.add(i);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return result;
    }

    public boolean removeThesis(int code, String storagePlace) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsThesis(statement, code)) {
                return false;
            }

            if (containsCopies(statement, code, storagePlace) <= -1) {
                return false;
            }

            String query = "DELETE FROM Physical_copies "
                    + "WHERE code = " + code + ";";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                return false;
            }

            query
                    = "SELECT P.number_of_copies"
                    + "FROM Physical_copies P"
                    + "WHERE P.code = '" + code + "'; ";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return true;
            } else{
                throw new DataAccessException("Error executing query!", null);
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

}

/*query = "DELETE FROM Book "
        + "WHERE code = " + code + ";";
        statement.executeQuery(query);
query = "DELETE FROM Thesis "
        + "WHERE code = " + code + ";";
        statement.executeQuery(query);
            return true;*/