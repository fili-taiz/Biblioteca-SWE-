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
                    + "FROM Hirer H LEFT JOIN Banned_hirer B ON H.user_code=B.user_code "
                    + "WHERE H.user_code = '" + userCode + "';";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                LocalDate unbannedDate = null;
                if(resultSet.getDate("unbanned_date") != null){
                    unbannedDate = resultSet.getDate("unbanned_date").toLocalDate();
                }
                return new Hirer(userCode, resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), null, unbannedDate);
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public HashMap<String, String> getSaltAndHashedPassword(String userCode) {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT HC.salt, HC.hashed_password "
                    + "FROM Hirer_credentials HC"
                    + "WHERE HC.usercode = '" + userCode + "';";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                HashMap<String, String> saltAndHashedPassword = new HashMap<>();
                saltAndHashedPassword.put("salt", resultSet.getString("salt"));
                saltAndHashedPassword.put("hashedPassword", resultSet.getString("hashed_password"));
                return saltAndHashedPassword;
            }else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addHirer(String userCode, String name, String surname, String email, String telephoneNumber) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO Hirer (user_code, name, surname, email, telephone_number)"
                    + "VALUES ('" + userCode + "', '" + name + "', '" + surname + "', '" + email + "', '" + telephoneNumber + "');";
            Statement statement = connection.createStatement();
            if(statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addHirerPassword(String userCode, String hashedPassword, String salt) {
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO User_credentials (user_code, hashed_password, salt)"
                    + "VALUES ('" + userCode + "', '" + hashedPassword + "', '" + salt + "');";
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(query) <= 0){
                throw new CRUD_exception("Error executing insert!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public ListOfHirers getHirers() {
        ArrayList<Hirer> result = new ArrayList<>();
        connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Hirer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            while (resultSet.next()) {
                LocalDate unbannedDate = null;
                if(resultSet.getDate("unbanned_date") != null){
                    unbannedDate = resultSet.getDate("unbanned_date").toLocalDate();
                }
                result.add(new Hirer(resultSet.getString("user_code"), resultSet.getString("name"), resultSet.getString("surname"),
                        resultSet.getString("email"), resultSet.getString("telephone_number"), null, unbannedDate));
            }
            ListOfHirers listOfHirers = new ListOfHirers(result);
            return listOfHirers;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

}
