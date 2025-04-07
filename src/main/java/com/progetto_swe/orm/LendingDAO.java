package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Lending;
import com.progetto_swe.domain_model.Library;
import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

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
                    + "FROM Physical_copies P"
                    + "WHERE P.code = '" + code + "' AND i.storage_place = '" + storagePlace + "'; ";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("number_of_copies");
            } else{
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    private boolean containsHirer(Statement statement, String userCode) {
        try {
            String query
                    = "SELECT *"
                    + "FROM Hirer H "
                    + "WHERE H.user_code = " + userCode + "; ";
            ResultSet resultSet = statement.executeQuery(query);
            if(!resultSet.next()) {
                throw new DataAccessException("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            String query = "INSERT INTO Lendings (user_code, code, storage_place, lenfing_date)"
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

    public ArrayList<Lending> getLendings(String hirerCode) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM Lendings WHERE userCode = '" + hirerCode + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            ArrayList<Lending> lendings = new ArrayList<>();
            while(resultSet.next()){
                lendings.add(new Lending(resultSet.getInt("code"), r))
            }


            /**/
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
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
            throw new DatabaseConnectionException("Connection error!", e);
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
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing query!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
