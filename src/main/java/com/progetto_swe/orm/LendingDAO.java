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

    public boolean addLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO lending (user_code, code, storage_place, lending_date) VALUES (?, ?, ?, ?); ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    public ListOfLendings getLendings_() {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM lending;";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Lending> lendings = new ArrayList<>();
            while (resultSet.next()) {
                BookDAO bookDAO = new BookDAO();
                MagazineDAO magazineDAO = new MagazineDAO();
                Book book = bookDAO.getBook(resultSet.getInt("code"));
                Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));

                HirerDAO hirerDAO = new HirerDAO();
                Hirer hirer = hirerDAO.getHirer(resultSet.getString("user_code"));
                Item item;
                if(book != null) {
                    item = book;
                } else if (magazine != null) {
                    item = magazine;
                } else {
                    return null;
                }
                lendings.add(new Lending(resultSet.getDate("lending_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
            }
            return new ListOfLendings(lendings);

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public boolean removeLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM lending L WHERE user_code = ? AND code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}
