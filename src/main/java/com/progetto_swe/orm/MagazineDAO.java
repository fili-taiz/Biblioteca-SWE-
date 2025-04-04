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

public class MagazineDAO {

    private Connection connection;

    public MagazineDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public int addMagazine(String title, String publicationDate, String language, String category, String link,
                           String publishingHouse) {

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
                throw new CRUD_exception("Error executing query!", null);
            }
            int code = resultSet.getInt("code");

            query
                    = "INSERT INTO Magazine (code, publishing_house)"
                    + "VALUES ('" + code + "', '" + publishingHouse + "');";
            statement.executeUpdate(query); //si chiama executeUpdate ma vale per INSERT, DELETE e UPDATE

            return code;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeMagazine(int code) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            //Creazione Item e Book
            String query
                    = "DELETE FROM Item "
                    + "WHERE code = '" + code + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }

            query
                    = "DELETE FROM Magazine "
                    + "where code = '" + code + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }

            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error", e);
        }
    }


    public Magazine getMagazine(String itemId) {
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return null;
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category, String link,
                                  String publishingHouse) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsMagazine(statement, originalItemCode)) {
                return false;
            }
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', language = '" + language + "', category = '" + category + "', link = '" + link + "' "
                    + "WHERE code = '" + originalItemCode + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }

            query
                    = "UPDATE Magazine "
                    + "SET publishing_house = '" + publishingHouse + "' "
                    + "WHERE code = '" + originalItemCode + "';";

            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing query!", null);
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return false;
    }

    private boolean containsMagazine(Statement statement, int code) {
        try {
            String query
                    = "SELECT *"
                    + "FROM (SELECT * FROM Item WHERE I.code = " + code + ") I JOIN Magazine M ON I.code = M.code ";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    private int containsMagazine(Statement statement, String title, String publicationDate, String borrowable, String language, String category,
                                 String link, String publishingHouse) {
        try {
            String query
                    = "SELECT code"
                    + "FROM Item I JOIN Magazine M ON I.code = M.code"
                    + "WHERE I.title = '" + title + "' AND I.publicationDate = '" + publicationDate + "' AND I.borrowable = " + borrowable
                    + " AND I.language = '" + language + "' AND I.category = '" + category + "' AND I.link = '" + link + "'"
                    + " AND M.publishing_house = '" + publishingHouse + "';";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("code");
            }else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
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

    public int addMagazine(String title, String publicationDate, String borrowable, String language, String category, String link,
                           String publishingHouse, String storagePlace, int numberOfCopies) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            int code = containsMagazine(statement, title, publicationDate, borrowable, language, category, link, publishingHouse);
            int copiesStored = containsCopies(statement, code, storagePlace);

            //libro non presente
            if (code <= -1) {
                //Creazione Item e Book
                String query = "INSERT INTO Item (title, publication_date, borrowable, language, category, link)"
                        + "VALUES ('" + title + "', '" + publicationDate + "', " + borrowable + ", '" + language + "', '" + category + "', '" + link + "') "
                        + "RETURNING code;";
                ResultSet resultSet = statement.executeQuery(query);
                if (!resultSet.next()) {
                    throw new DataAccessException("Error executing query!", null);
                }
                code = resultSet.getInt("code");
                query = "INSERT INTO Magazine (code, publishing_house)"
                        + "VALUES ('" + code + "', '" + publishingHouse + "');";
                ResultSet rs = statement.executeQuery(query);
                if(!rs.next()){
                    throw new CRUD_exception("Error executing query!", null);
                }
                return code;
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
                    throw new CRUD_exception("Error executing query!", null);
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
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return -1;
    }

    public boolean removeMagazine(int code, String storagePlace) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsMagazine(statement, code)) {
                return false;
            }

            if (containsCopies(statement, code, storagePlace) <= -1) {
                return false;
            }

            String query = "DELETE FROM Phisycal_copies "
                    + "WHERE code = " + code + ";";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                throw new CRUD_exception("Error executing query!", null);
            }

            query
                    = "SELECT P.number_of_copies"
                    + "FROM Physical_copies P"
                    + "WHERE P.code = '" + code + "'; ";
            resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }

            query = "DELETE FROM Magazine "
                    + "WHERE code = " + code + ";";
            statement.executeQuery(query);
            query = "DELETE FROM Item "
                    + "WHERE code = " + code + ";";

            statement.executeQuery(query);
            return true;
            //QUI NON SO SE METTERE L'ECCEZIONE

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);

        }
    }


    public ArrayList<Item> getAllMagazine() {
        ArrayList<Item> result = new ArrayList<>();
        HashMap<Library,PhysicalCopies> physicalCopies;
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Magazine M ON I.code = M.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            while (resultSet.next()) {
                Item i = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"),
                        resultSet.getString("publishingHouse"));
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

}
