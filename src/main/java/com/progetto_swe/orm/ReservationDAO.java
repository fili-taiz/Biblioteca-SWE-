package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }
//TODO guarda LendingDAO
    public void addReservation(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Reservation (user_code, code, storage_place, reservation_date)"
                    + "VALUES (?, ?, ?, ?); ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            //return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false;
        }
    }

    public void removeReservation(String userCode, int itemCode, String storagePlace){
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Reservation R WHERE user_code = ? AND code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            //return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            //return false;
        }
    }

    public ArrayList<Reservation> getReservationsByUserCode(String userCode) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM Reservation R WHERE R.user_code = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                BookDAO bookDAO = new BookDAO();
                MagazineDAO magazineDAO = new MagazineDAO();
                Book book = bookDAO.getBook(resultSet.getInt("code"));
                Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));

                HirerDAO hirerDAO = new HirerDAO();
                Hirer hirer = hirerDAO.getHirer(userCode);
                Item item;
                if (book != null) {
                    item = book;
                } else if (magazine != null) {
                    item = magazine;
                } else {
                    return null;
                }
                reservations.add(new Reservation(resultSet.getDate("lending_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
            }
            return reservations;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    //public ArrayList<Reservation> getReservationsByStoragePlace(String storagePlace) {
    //    this.connection = ConnectionManager.getConnection();
    //    try {
    //        String query = "SELECT * FROM Reservation R WHERE R.storage_place = ?;";
    //        PreparedStatement ps = connection.prepareStatement(query);
    //        ps.setString(1, storagePlace);
    //        ResultSet resultSet = ps.executeQuery();
    //        ArrayList<Reservation> reservations = new ArrayList<>();
    //        while (resultSet.next()) {
    //            BookDAO bookDAO = new BookDAO();
    //            MagazineDAO magazineDAO = new MagazineDAO();
    //            Book book = bookDAO.getBook(resultSet.getInt("code"));
    //            Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));
//
    //            HirerDAO hirerDAO = new HirerDAO();
    //            Hirer hirer = hirerDAO.getHirer(storagePlace);
    //            Item item;
    //            if (book != null) {
    //                item = book;
    //            } else if (magazine != null) {
    //                item = magazine;
    //            } else {
    //                return null;
    //            }
    //            reservations.add(new Reservation(resultSet.getDate("lending_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
    //        }
    //        return reservations;
    //    } catch (SQLException e) {
    //        System.out.println("SQLException: " + e.getMessage());
    //        return null;
    //    }
    //}
}
