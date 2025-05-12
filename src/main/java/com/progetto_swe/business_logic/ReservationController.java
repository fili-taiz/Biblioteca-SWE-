package com.progetto_swe.business_logic;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.ReservationDAO;
import net.bytebuddy.utility.nullability.NeverNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class ReservationController {
    public ArrayList<Reservation> getReservation(String userCode) {
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservationsByUserCode(userCode);
    }


    public boolean removeReservation(Hirer hirer, Item item, Library storagePlace, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }
        if(!token.getTokenWorkingPlace().equals(storagePlace.toString())){
            //TODO lancia eccezione
        }
        ReservationDAO reservationDAO = new ReservationDAO();

        //if(!listOfReservations.haveReservation(reservation)){
        //    return false;
        //} controllo da eccezione

        //if(reservation.getHirer()!=this.hirer){
        //    return false;
        //}

        reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), storagePlace.toString());
        return true;
    }


    public void reserveItem(Hirer hirer, Item item, Library storagePlace, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Hirer"))){
            //TODO lancia eccezione
        }
        if(hirer.getUnbannedDate() != null){
            //return false;//TODO lancia eccezione
        }
        ReservationDAO reservationDAO = new ReservationDAO();
        try {
            reservationDAO.addReservation(hirer.getUserCode(), item.getCode(), storagePlace.name());
            MailSender.sendReservationSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(), storagePlace.toString(), LocalDate.now().plusDays(7));
        } catch (Exception e){
        }
    }

    public void confirmReservationWithdraw(Hirer hirer, Item item, Library storagePlace, Token token) {
        Objects.requireNonNull(hirer);//TODO aggiungi requireNonNull

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }
        if(!token.getTokenWorkingPlace().equals(storagePlace.toString())){
            //TODO lancia eccezione
        }
        //if(!reservationDAO.getReservations_().haveReservation(reservation)){
        //    return false;
        //} controllo da eccezione

        /*cancella reservation */
        ConnectionManager.closeAutoCommit();
        ReservationDAO reservationDAO = new ReservationDAO();
        LendingController lendingController = new LendingController();
        try {
            reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), storagePlace.toString());
        } catch (Exception e){
            ConnectionManager.rollback();//TODO eccezione
        }

        try {
            lendingController.registerLending(hirer, item, token);
            ConnectionManager.commit();
            MailSender.sendWithdrawSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(), token.getTokenWorkingPlace(), LocalDate.now().plusMonths(1).toString());
        }catch (Exception e){
            ConnectionManager.rollback();//TODO eccezione
        }
    }
}
