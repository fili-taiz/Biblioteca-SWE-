package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
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

    public Magazine getMagazine(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * FROM Item I JOIN Magazine M ON I.code = M.code WHERE I.code = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }
            Magazine magazine = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                    Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"),
                    resultSet.getString("publishingHouse"));
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            magazine.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(code));
            return magazine;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public int addMagazine(String title, String publicationDate, String language, String category, String link, String publishingHouse) {

        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Magazine
            String query
                    = "INSERT INTO Item (title, publication_date, language, category, link)"
                    + "VALUES (?, ?, ?, ?, ?) "
                    + "RETURNING code;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, publicationDate);
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ResultSet resultSet = ps.executeQuery(query);
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing insert!", null);
            }
            int code = resultSet.getInt("code");

            query = "INSERT INTO Magazine (code, publishing_house) VALUES (?, ?);";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, publishingHouse);
            if(ps.executeUpdate() <= 0){
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
            String query = "DELETE FROM Item WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            if(ps.executeUpdate() <= 0){
                throw new CRUD_exception("Error executing delete!", null);
            }

            query = "DELETE FROM Magazine WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            if(ps.executeUpdate() <= 0){
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
            //TODO guarda se ho controllato che questo magazine sia dentro al catalogue;
            String query
                    = "UPDATE Item SET title = ?, publication_date = ?, language = ?, category = ?, link = ? WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, publicationDate);
            ps.setString(3, language);
            ps.setString(4, category);
            ps.setString(5, link);
            ps.setInt(6, originalItemCode);
            if(ps.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }

            query = "UPDATE Magazine SET publishing_house = ? WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, publishingHouse);
            ps.setInt(1, originalItemCode);

            if(ps.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing update!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}