package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public void addReservation(String userCode, int itemCode, String storagePlace)
            throws IdAlreadyExistsException, DatabaseConnectionException {
        this.connection = ConnectionManager.getConnection();
        try {
            String query = """ 
                    INSERT INTO Reservation (user_code, code, storage_place, reservation_date)
                    VALUES (?, ?, ?, ?); 
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            if(e.getSQLState().equals("23505")){
                throw new IdAlreadyExistsException("Errore: Hirer ha già prenotato un articolo con itemCode [" + userCode + "].");
            }
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public void removeReservation(String userCode, int itemCode, String storagePlace)
            throws IdNotFoundException, DatabaseConnectionException {
        this.connection = ConnectionManager.getConnection();
        ConnectionManager.closeAutoCommit();
        try {
            String query = """ 
                    DELETE FROM Reservation R 
                    WHERE user_code = ? AND code = ? AND storage_place = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ps.setInt(2, itemCode);
            ps.setString(3, storagePlace);
            if(ps.executeUpdate() != 1) {
                ConnectionManager.rollback();
                throw new IdNotFoundException("Errore: Prestito di Hirer con userCode [" + itemCode + "] e Item con itemCode [" + itemCode + "] presso sede [" + storagePlace + "] non presente nel DB.");
            }
            ConnectionManager.commit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }

    public ArrayList<Reservation> getReservationsByUserCode(String userCode)
            throws IdNotFoundException, DatabaseConnectionException{
        this.connection = ConnectionManager.getConnection();
        BookDAO bookDAO = new BookDAO();
        MagazineDAO magazineDAO = new MagazineDAO();
        try {
            String query = """
                    SELECT * 
                    FROM Reservation R 
                    WHERE R.user_code = ?;
                    """;
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userCode);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                HirerDAO hirerDAO = new HirerDAO();
                Hirer hirer = hirerDAO.getHirer(userCode);
                try {
                    Book book = bookDAO.getBook(resultSet.getInt("code"));
                    reservations.add(new Reservation(
                            resultSet.getDate("reservation_date").toLocalDate(), hirer, book,
                                    Library.valueOf(resultSet.getString("storage_place"))));
                }catch (IdNotFoundException e) {
                }
                try {
                    Magazine magazine = magazineDAO.getMagazine(resultSet.getInt("code"));
                    reservations.add(new Reservation(resultSet.getDate("reservation_date").toLocalDate(), hirer, magazine, Library.valueOf(resultSet.getString("storage_place"))));
                }catch (IdNotFoundException e) {
                }
            }
            return reservations;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getCause().toString());
        }
    }
}
