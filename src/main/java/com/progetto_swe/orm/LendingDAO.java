package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;

import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class LendingDAO {

    private Connection connection;

    public LendingDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public boolean addLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();

            String query = "INSERT INTO Lendings (user_code, code, storage_place, lenfing_date)" + "VALUES ('" + userCode + "', '" + itemCode + "', " + storagePlace + ", '" + LocalDate.now() + "'); ";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                throw new CRUD_exception("Error executing insert!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public ListOfLendings getLendings() {
        this.connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Lendings;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
            ArrayList<Lending> lendings = new ArrayList<>();
            while (resultSet.next()) {
                BookDAO bookDAO = new BookDAO();
                ThesisDAO thesisDAO = new ThesisDAO();
                MagazineDAO magazineDAO = new MagazineDAO();
                Book book = bookDAO.getBook(resultSet.getInt("code"));
                Thesis thesis = thesisDAO.getThesis(resultSet.getInt("code"));
                Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));

                HirerDAO hirerDAO = new HirerDAO();
                Hirer hirer = hirerDAO.getHirer(resultSet.getString("user_code"));
                Item item;
                if(book != null) {
                    item = book;
                } else if (thesis != null) {
                    item = thesis;
                } else if (magazine != null) {
                    item = magazine;
                } else {
                    return null;
                }
                lendings.add(new Lending(resultSet.getDate("lending_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
            }
            ListOfLendings listOfLendings = new ListOfLendings(lendings);
            return listOfLendings;

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Lending L " + "WHERE user_code = '" + userCode + "' AND code = " + itemCode + "AND storage_place = '" + storagePlace + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
