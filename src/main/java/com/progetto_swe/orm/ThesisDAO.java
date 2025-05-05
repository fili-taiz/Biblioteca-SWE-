package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
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

    public Thesis getThesis(int code) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT * FROM Item I JOIN Thesis T ON I.code = T.code WHERE I.code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()) {
                //System.out.println("There is no book in the database with code = " + code + "!");
                return null;
            }
            Thesis thesis = new Thesis(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")),
                    Language.valueOf(resultSet.getString("language")), Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"), resultSet.getString("author"),
                    resultSet.getString("supervisors"), resultSet.getString("university"));
            PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
            thesis.setPhysicalCopies(physicalCopiesDAO.getPhysicalCopies(code));
            return thesis;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public int addThesis(String title, String publicationDate, String language, String category, String link, int number_of_pages, String author, String supervisors, String university) {

        connection = ConnectionManager.getConnection();
        try {
            //Creazione Item e Thesis
            String query = "INSERT INTO Item (title, publication_date, language, category, link, number_of_pages) VALUES (?, ?, ?, ?, ?, ?) "
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

            query = "INSERT INTO Thesis (code, author, supervisors, university) VALUES (?, ?, ?, ?);";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, author);
            ps.setString(3, supervisors);
            ps.setString(4, university);
            ps.executeUpdate();

            return code;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return -1;
        }
    }

    public boolean removeThesis(int code) {

        connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Thesis WHERE code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            if(ps.executeUpdate() == 0){
                return false;
            }

            query = "DELETE FROM Item WHERE code = ?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    public boolean updateThesis(int originalItemCode,String title, String publicationDate, String language, String category, String link, String author,
                                String supervisors, String university) {
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "UPDATE Item SET title = ?, publication_date = ?, language = ?, category = ?, link = ? WHERE code = ? ;";
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

            query = "UPDATE Thesis SET author = ?, supervisors = ?, university = ? WHERE code = ?;";;
            ps = connection.prepareStatement(query);
            ps.setString(1, author);
            ps.setString(2, supervisors);
            ps.setString(3, university);
            ps.setInt(4, originalItemCode);

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}