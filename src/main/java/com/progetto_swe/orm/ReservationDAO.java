package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Reservation;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Reservation getReservation(String itemCode, Library LIBRARY, String hirerCode){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/


        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return null;
    }

    public Reservation updateReservation(Reservation reservation){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/


        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return null;
    }

    private int containsCopies(Statement statement, int code, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT P.number_of_copies - "
                    + " - (SELECT COUNT(*) FROM Lendings L WHERE L.code = " + code + " AND L.storage_place = '" + storagePlace + "')"
                    + " - (SELECT COUNT(*) FROM Reservation R WHERE R.code = " + code + " AND R.storage_place = '" + storagePlace + "')"
                    + "FROM Phisical_copies P"
                    + "WHERE P.code = '" + code + "' AND P.storage_place = '" + storagePlace + "'; ";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                resultSet.getInt("number_of_copies");
            }else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return -1;
    }

    private boolean containsHirer(Statement statement, String userCode) {
        try {
            String query
                    = "SELECT *"
                    + "FROM Hirer H "
                    + "WHERE H.user_code = " + userCode + "; ";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addReservation(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if(!containsHirer(statement, userCode)){
                return false;
            }

            if(containsCopies(statement, itemCode, storagePlace) <= 1){
                return false;
            }

            String query = "INSERT INTO Reservation (user_code, code, storage_place, lending_date)"
                    + "VALUES ('" + userCode + "', '" + itemCode + "', " + storagePlace + ", '" + LocalDate.now() + "'); ";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeReservation(String userCode, int itemCode, String storagePlace){
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Reservation R "
                    + "WHERE user_code = '" + userCode + "' AND code = " + itemCode + "AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            if(statement.executeUpdate(query) != 1){
                throw new CRUD_exception("Error executing query!", null);
            }
            return true;

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
