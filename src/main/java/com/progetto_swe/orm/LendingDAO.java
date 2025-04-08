package com.progetto_swe.orm;

import java.sql.*;
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

    private int containsCopies(PreparedStatement ps, int code, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT P.number_of_copies - "
                    + " - (SELECT COUNT(*) FROM Lendings L WHERE L.code = ? AND L.storage_place = ?)"
                    + " - (SELECT COUNT(*) FROM Reservation R WHERE R.code = ? AND R.storage_place = ?)"
                    + "FROM Physical_copies P WHERE P.code = ? AND i.storage_place = ?; ";
            ps.setInt(1, code);
            ps.setString(2, storagePlace);
            ps.setInt(3, code);
            ps.setString(4, storagePlace);
            ps.setInt(5, code);
            ps.setString(6, storagePlace);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("number_of_copies");
            } else {
                throw new DataAccessException("Error executing query!", null);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    private boolean containsHirer(PreparedStatement ps, String userCode) {
        try {
            String query = "SELECT * FROM Hirer H WHERE H.user_code = ?;";
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
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

            String query = "INSERT INTO Lendings (user_code, code, storage_place, lending_date) VALUES (?, ?, ?, ?); ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));

            ResultSet resultSet = ps.executeQuery();
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
            String query = "SELECT * FROM Lendings;";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
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

    /*public boolean updateLending(Lending lending) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();


        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
        return false;
    }*/


    public boolean removeLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Lending L WHERE user_code = ? AND code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
