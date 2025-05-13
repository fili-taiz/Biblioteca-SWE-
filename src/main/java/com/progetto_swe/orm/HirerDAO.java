package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.*;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class HirerDAO {

    private Connection connection;

    //creazione Hirer con solo i dati inerenti Hirer
    public Hirer getHirer(String userCode) throws IdNotFoundException, DatabaseConnectionException {
        try {
            connection = ConnectionManager.getConnection();
            String query
                    = "SELECT * "
                    + "FROM hirer H LEFT JOIN banned_hirers B ON H.user_code=B.user_code "
                    + "WHERE H.user_code = ?;";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Hirer con userCode [" + userCode + "] non è presente nel DB.");
            }

            LocalDate unbannedDate = null;
            if(resultSet.getDate("unbanned_date") != null){
                unbannedDate = resultSet.getDate("unbanned_date").toLocalDate();
            }

            Hirer hirer = new Hirer(
                    userCode,
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("email"),
                    resultSet.getString("telephone_number"),
                    null,
                    unbannedDate);
            hirer.setToken(new Token(hirer));
            return hirer;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public HashMap<String, String> getSaltAndHashedPassword(String userCode) throws IdNotFoundException, DatabaseConnectionException {
        try {
            connection = ConnectionManager.getConnection();
            String query = "SELECT UC.salt, UC.hashed_password FROM user_credentials UC WHERE UC.user_code = ?;";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.next()) {
                throw new IdNotFoundException("Errore: Credenziali per Hirer con userCode [" + userCode + "] non presenti nel DB.");
            }

            HashMap<String, String> saltAndHashedPassword = new HashMap<>();
            saltAndHashedPassword.put("salt", resultSet.getString("salt"));
            saltAndHashedPassword.put("hashedPassword", resultSet.getString("hashed_password"));
            return saltAndHashedPassword;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void addHirer(String userCode, String name, String surname, String email, String telephoneNumber) throws IdAlreadyExistsException, DatabaseConnectionException {
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO Hirer (user_code, name, surname, email, telephone_number) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.setString(5, telephoneNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Hirer con userCode [" + userCode + "] già presente nel DB.");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void addHirerPassword(String userCode, String hashedPassword, String salt) throws IdAlreadyExistsException, DatabaseConnectionException {
        try {
            connection = ConnectionManager.getConnection();
            String query = "INSERT INTO user_credentials (user_code, hashed_password, salt) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setString(2, hashedPassword);
            ps.setString(3, salt);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Credenziali per Hirer con userCode [" + userCode + "] già presenti nel DB.");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public ArrayList<Hirer> getHirers_() throws DatabaseConnectionException {
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
            return result;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

}
