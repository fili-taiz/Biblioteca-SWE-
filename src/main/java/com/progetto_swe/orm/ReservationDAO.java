package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;

import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public ListOfReservations getReservations() {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "SELECT * FROM Reservations;";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            ArrayList<Reservation> reservations = new ArrayList<>();
            if(!resultSet.next()){
                throw new DataAccessException("Error executing query!", null);
            }
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
                reservations.add(new Reservation(resultSet.getDate("reservation_date").toLocalDate(), hirer, item, Library.valueOf(resultSet.getString("storage_place"))));
            }

            ListOfReservations listOfReservations = new ListOfReservations(reservations);
            return listOfReservations;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean addReservation(String userCode, int itemCode, String storagePlace) {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Reservation (user_code, code, storage_place, reservation_date)"
                    + "VALUES (?, ?, ?, ?); ";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()){
                throw new CRUD_exception("Error executing insert!", null);
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }

    public boolean removeReservation(String userCode, int itemCode, String storagePlace){
        this.connection = ConnectionManager.getConnection();
        try {
            String query = "DELETE FROM Reservation R WHERE user_code = ? AND code = ? AND storage_place = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            if(ps.executeUpdate() != 1){
                throw new CRUD_exception("Error executing delete!", null);
            }
            return true;

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection error!", e);
        }
    }
}
