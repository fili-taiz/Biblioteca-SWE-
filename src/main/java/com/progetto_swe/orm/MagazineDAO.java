package com.progetto_swe.orm;

import java.sql.*;
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
//TODO stesse modifiche di eccezioni e paramtri di BookDAO
    public Magazine getMagazine(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * FROM Item I JOIN Magazine M ON I.code = M.code WHERE I.code = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
                //System.out.println("There is no book in the database with code = " + code + "!");
                return null;
            }
            Magazine magazine = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"),
                    resultSet.getString("publishing_house"));
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            magazine.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(code));
            return magazine;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public int addMagazine(String title, String publicationDate, String language, String category, String link, String publishingHouse, int number_of_pages) {

        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Magazine
            String query
                    = "INSERT INTO Item (title, publication_date, language, category, link, number_of_pages)"
                    + " VALUES (?, ?, ?, ?, ?, ?) "
                    + " RETURNING code;";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setDate(2, Date.valueOf(publicationDate));
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, number_of_pages);

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new CRUD_exception("Error executing insert!", null);
            }

            int code = generatedKeys.getInt(1);

            query = "INSERT INTO Magazine (code, publishing_house) VALUES (?, ?);";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, publishingHouse);

            ps.executeUpdate();

            return code;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return -1;
        }
    }

    public void removeMagazine(int code) {

        connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Magazine WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            //if(ps.executeUpdate() == 0){
            //    return false;
            //}

            query = "DELETE FROM Item WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            //return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false;
        }
    }

    public void updateMagazine(int originalItemCode, String title, String publicationDate, String language, String category, String link, String publishingHouse) {
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

            //if(ps.executeUpdate() == 0){
            //    return false;
            //}

            query = "UPDATE Magazine SET publishing_house = ? WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, publishingHouse);
            ps.setInt(2, originalItemCode);

            //return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false;
        }
    }

    public ArrayList<Magazine> getAllMagazines() {
        ArrayList<Magazine> magazines = new ArrayList<>();
        HashMap<Library, PhysicalCopies> physicalCopies;
        connection = ConnectionManager.getConnection();
        try {
            //tutti i magazine
            String query
                    = "SELECT * FROM Item I JOIN Magazine M ON I.code = M.code";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                magazines.add(new Magazine(resultSet.getInt("code"),
                        resultSet.getString("title"),
                        LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")),
                        resultSet.getString("link"),
                        resultSet.getInt("number_of_pages"),
                        resultSet.getString("publishing_house")));
            }

            for(Magazine magazine : magazines){
                String query_2 = "SELECT * FROM physical_copies P WHERE P.code = ?;";
                ps = connection.prepareStatement(query_2);
                ps.setInt(1, magazine.getCode());
                ResultSet copiesSet = ps.executeQuery();
                physicalCopies = new HashMap<>();
                while (copiesSet.next()) {
                    physicalCopies.put(
                            Library.valueOf(copiesSet.getString("storage_place")),
                            new PhysicalCopies(copiesSet.getInt("number_of_copies"),
                                    copiesSet.getInt("number_of_available_copies"),
                                    copiesSet.getBoolean("borrowable")));
                }
                magazine.setPhysicalCopies(physicalCopies);
            }
            return magazines;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
}