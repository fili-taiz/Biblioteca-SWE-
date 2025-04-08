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

public class MagazineDAO {

    private Connection connection;

    public MagazineDAO(Connection connection) {
        this.connection = connection;
    }

    public Magazine getMagazine(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Magazine M ON I.code = M.code"
                    + "WHERE I.code = " + code + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }
            Magazine magazine = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"),
                    resultSet.getString("publishingHouse"));
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
                magazine.setPhysicalCopies(physicalCopies);
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
            return magazine;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public int addMagazine(String title, String publicationDate, String language, String category, String link, String publishingHouse) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            //Creazione Item e Magazine
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
                    = "INSERT INTO Magazine (code, publishing_house)"
                    + "VALUES (" + code + ", '" + publishingHouse + "');";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }

            return code;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeMagazine(int code) {

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
                    = "DELETE FROM Magazine "
                    + "where code = " + code + ";";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error", e);
        }
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category, String link,
                                  String publishingHouse) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            //TODO guarda se ho controllato che questo magazine sia dentro al catalogue;
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', language = '" + language + "', category = '" + category + "', link = '" + link + "' "
                    + "WHERE code = '" + originalItemCode + "';";
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }

            query
                    = "UPDATE Magazine "
                    + "SET publishing_house = '" + publishingHouse + "' "
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