package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.progetto_swe.domain_model.Library;
import com.progetto_swe.domain_model.Reservation;

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
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
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
            }
        } catch (SQLException e) {
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
            return resultSet.next();
        } catch (SQLException e) {
        }
        return false;
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
            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public boolean removeReservation(String userCode, int itemCode, String storagePlace){
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Reservation R "
            + "WHERE user_code = '" + userCode + "' AND code = " + itemCode + "AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }
}
