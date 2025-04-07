package com.progetto_swe.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;

import com.progetto_swe.orm.database_exception.CRUD_exception;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public ListOfReservations getReservations() {
        this.connection = ConnectionManager.getConnection();
        try {
            String query
                    = "SELECT * "
                    + "FROM Reservations;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            ArrayList<Reservation> reservations = new ArrayList<>();
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
            Statement statement = connection.createStatement();
            String query = "INSERT INTO Reservation (user_code, code, storage_place, reservation_date)"
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
