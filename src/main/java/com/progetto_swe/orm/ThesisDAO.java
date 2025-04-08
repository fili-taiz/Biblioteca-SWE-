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

public class ThesisDAO {

    private Connection connection;

    public ThesisDAO(Connection connection) {
        this.connection = connection;
    }

    public Thesis getThesis(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Thesis T ON I.code = T.code"
                    + "WHERE I.code = " + code + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }
            Thesis thesis = new Thesis(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")),
                    Language.valueOf(resultSet.getString("language")), Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"), resultSet.getString("author"),
                    resultSet.getString("supervisors"), resultSet.getString("university"));
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
                thesis.setPhysicalCopies(physicalCopies);
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
            return thesis;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
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
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing insert!", null);
            }
            int code = resultSet.getInt("code");

            query
                    = "INSERT INTO Thesis (code, author, supervisors, university)"
                    + "VALUES ('" + code + "', '" + author + "', '" + supervisors + "', '" + university + "');";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }//si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE

            return code;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeThesis(int code) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            String query
                    = "DELETE FROM Item "
                    + "WHERE code = " + code + ";";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            }

            query
                    = "DELETE FROM Thesis "
                    + "where code = " + code + ";";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean updateThesis(int originalItemCode,String title, String publicationDate, String language, String category, String link, String author,
                                String supervisors, String university) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            //TODO guarda se ho controllato che questo thesis sia dentro al catalogue;
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = " + publicationDate + ", language = '" + language + "', category = '" + category + "', link = '" + link + "'"
                    + "WHERE code = '" + originalItemCode + "';";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }

            query
                    = "UPDATE Thesis "
                    + "SET author = '" + author + "', supervisors = '" + supervisors + "', university = '" + university + "'"
                    + "WHERE code = '" + originalItemCode + "';";;

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}