package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.progetto_swe.domain_model.Lending;
import com.progetto_swe.domain_model.Library;

public class LendingDAO {

    private Connection connection;

    public LendingDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    private int containsCopies(Statement statement, int code, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT P.number_of_copies - " 
                    + " - (SELECT COUNT(*) FROM Lendings L WHERE L.code = " + code + " AND L.storage_place = '" + storagePlace + "')"
                    + " - (SELECT COUNT(*) FROM Reservation R WHERE R.code = " + code + " AND R.storage_place = '" + storagePlace + "')"
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

    public boolean addLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            if(!containsHirer(statement, userCode)){
                return false;
            }

            if(containsCopies(statement, itemCode, storagePlace) <= 1){
                return false;
            }

            String query = "INSERT INTO Lendings (user_code, code, storage_place, lenfing_date)"
            + "VALUES ('" + userCode + "', '" + itemCode + "', " + storagePlace + ", '" + LocalDate.now() + "'); ";
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }

    public Lending getLending(String itemCode, Library LIBRARY, String hirerCode) {
        this.connection = ConnectionManager.getConnection();
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

    public boolean updateLending(Lending lending) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

            /**/
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return false;
    }



    public boolean removeLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Lending L "
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
