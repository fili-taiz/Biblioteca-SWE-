package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;

public class LendingDAO {

    private Connection connection;

    public LendingDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public void addLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO lending (user_code, code, storage_place, lending_date) VALUES (?, ?, ?, ?); ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusMonths(1)));
            //return ps.executeUpdate() != 0; TODO controllo se tupla non esistente restituisce 0 oppure lancia eccezione
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false; ipotetica eccezione
        }
    }

    public void removeLending(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM lending L WHERE user_code = ? AND code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            //return ps.executeUpdate() != 0; TODO controllo se tupla non esistente restituisce 0 oppure lancia eccezione
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false; TODO ipotetica eccezione
        }
    }

    //aggiunto da testare

    public ArrayList<Lending> getLendingsByUserCode(String userCode) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM lending L WHERE L.user_code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Lending> lendings = new ArrayList<>();
            while (resultSet.next()) {
                BookDAO bookDAO = new BookDAO();
                MagazineDAO magazineDAO = new MagazineDAO();
                Book book = bookDAO.getBook(resultSet.getInt("code"));
                Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));

                HirerDAO hirerDAO = new HirerDAO();
                Hirer hirer = hirerDAO.getHirer(userCode);
                Item item;
                if(book != null) {
                    item = book;
                } else if (magazine != null) {
                    item = magazine;
                } else {
                    return null;
                }
                lendings.add(new Lending(resultSet.getDate("lending_date").toLocalDate(), resultSet.getDate("maturity_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
            }
            return lendings;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null; //TODO ipotetica eccezione
        }
    }

    //public ArrayList<Lending> getLendingsByStoragePlace(String storagePlace) {
    //    this.connection = ConnectionManager.getConnection();
    //    try {
    //        String query = "SELECT * FROM lending L WHERE L.user_code = ?;";
    //        PreparedStatement ps = connection.prepareStatement(query);
    //        ps.setString(1, storagePlace);
    //        ResultSet resultSet = ps.executeQuery();
    //        ArrayList<Lending> lendings = new ArrayList<>();
    //        while (resultSet.next()) {
    //            BookDAO bookDAO = new BookDAO();
    //            MagazineDAO magazineDAO = new MagazineDAO();
    //            Book book = bookDAO.getBook(resultSet.getInt("code"));
    //            Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));
//
    //            HirerDAO hirerDAO = new HirerDAO();
    //            Hirer hirer = hirerDAO.getHirer(storagePlace);
    //            Item item;
    //            if(book != null) {
    //                item = book;
    //            } else if (magazine != null) {
    //                item = magazine;
    //            } else {
    //                return null;
    //            }
    //            lendings.add(new Lending(resultSet.getDate("lending_date").toLocalDate(), resultSet.getDate("maturity_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
    //        }
    //        return lendings;
    //    } catch (SQLException e) {
    //        System.out.println("SQLException: " + e.getMessage());
    //        return null;
    //    }
    //}
}
