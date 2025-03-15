package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Magazine;

public class MagazineDAO {

    private Connection connection;

    public MagazineDAO() {
        this.connection = ConnectionManager.getConnection();
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
            // TODO Auto-generated catch block
        }
        return null;
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, String borrowable, String language, String category, String link,
            String publishingHouse, String storagePlace, int numberOfCopies) {
        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if (!containsMagazine(statement, originalItemCode)) {
                return false;
            }
            String query
                    = "UPDATE Item "
                    + "SET title = '" + title + "', publication_date = '" + publicationDate + "', borrowable = " + borrowable
                    + ", language = '" + language + "', category = '" + category + "', link = '" + link + "' "
                    + "WHERE code = '" + originalItemCode + "';";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            query
                    = "UPDATE Magazine "
                    + "SET publishing_house = '" + publishingHouse + "' "
                    + "WHERE code = '" + originalItemCode + "';";
            statement.executeQuery(query);
            resultSet.next();

            if (containsCopies(statement, numberOfCopies, storagePlace) <= -1) {
                return true;
            }

            query
                    = "UPDATE Physical_copies "
                    + "SET number_of_copies = " + numberOfCopies
                    + "WHERE code = '" + originalItemCode + "' AND storage_place = '" + storagePlace + "'; ";
            resultSet = statement.executeQuery(query);
            resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    private boolean containsMagazine(Statement statement, int code) {
        try {
            String query
                    = "SELECT *"
                    + "FROM (SELECT * FROM Item WHERE I.code = " + code + ") I JOIN Magazine M ON I.code = M.code ";
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
        }
        return false;
    }

    private int cointainsMagazine(Statement statement, String title, String publicationDate, String borrowable, String language, String category,
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

    public int addMagazine(String title, String publicationDate, String borrowable, String language, String category, String link,
            String publishingHouse, String storagePlace, int numberOfCopies) {

        connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            int code = cointainsMagazine(statement, title, publicationDate, borrowable, language, category, link, publishingHouse);
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
                query = "INSERT INTO Magazine (code, publishing_house)"
                        + "VALUES ('" + code + "', " + publishingHouse + "');";
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

            query = "DELETE FROM Magazine "
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


    public ArrayList<Item> getAllMagazine() {
        ArrayList<Item> result = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Item I JOIN Magazine M ON I.code = M.code";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Item i = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getBoolean("borrowable"),
                        resultSet.getString("publishingHouse"));
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

}
