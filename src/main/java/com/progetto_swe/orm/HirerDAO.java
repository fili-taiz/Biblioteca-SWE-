package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class HirerDAO {

    private Connection connection;

    //creazione Hirer con solo i dati inerenti Hirer
    public Hirer getHirer(String userCode) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM hirer H LEFT JOIN banned_hirers B ON H.user_code=B.user_code "
                    + "WHERE H.user_code = ?;";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                LocalDate unbannedDate = null;
                if(resultSet.getDate("unbanned_date") != null){
                    unbannedDate = resultSet.getDate("unbanned_date").toLocalDate();
                }
                return new Hirer(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), null, unbannedDate);
            } else{
                throw new DataAccessException("There isn't any hirer in the database with usercode = " + userCode, null);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public HashMap<String, String> getSaltAndHashedPassword(String userCode) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT UC.salt, UC.hashed_password FROM user_credentials UC WHERE UC.user_code = ?;";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                HashMap<String, String> saltAndHashedPassword = new HashMap<>();
                saltAndHashedPassword.put("salt", resultSet.getString("salt"));
                saltAndHashedPassword.put("hashedPassword", resultSet.getString("hashed_password"));
                return saltAndHashedPassword;
            }else{
                throw new DataAccessException("There is no hirer in the database with usercode = " +userCode, null);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public boolean addHirer(String userCode, String name, String surname, String email, String telephoneNumber){
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO Hirer (user_code, name, surname, email, telephone_number) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.setString(5, telephoneNumber);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public boolean addHirerPassword(String userCode, String hashedPassword, String salt) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO user_credentials (user_code, hashed_password, salt) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, hashedPassword);
            ps.setString(3, salt);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    public ListOfHirers getHirers_() {
        ArrayList<Hirer> result = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM hirer H LEFT JOIN banned_hirers BH ON H.user_code = BH.user_code;";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                LocalDate unbannedDate = null;
                if(resultSet.getDate("unbanned_date") != null){
                    unbannedDate = resultSet.getDate("unbanned_date").toLocalDate();
                }
                result.add(new Hirer(resultSet.getString("user_code"), resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), null, unbannedDate));
            }
            if(result.isEmpty()){
                throw new DataAccessException("There aren't hirers in the database!", null);
            }
            return new ListOfHirers(result);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

}
