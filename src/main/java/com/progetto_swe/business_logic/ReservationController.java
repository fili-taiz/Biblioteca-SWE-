package com.progetto_swe.business_logic;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationController {

    public ArrayList<Reservation> getReservations(String userCode) {
        ReservationDAO reservationDAO = new ReservationDAO();
        return reservationDAO.getReservationsByUserCode(userCode);
    }


    public boolean removeReservation(Hirer hirer, Item item, String storagePlace, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        if(!token.getTokenWorkingPlace().equals(storagePlace)){
            throw new ActionDeniedException("Errore: Non puoi registrare questa operazione, il libro non è stato prenotato nella sede in cui lavori.");
        }
        Library.valueOf(storagePlace);
        ReservationDAO reservationDAO = new ReservationDAO();

        reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), storagePlace);
        WaitingListDAO waitingListDAO = new WaitingListDAO();
        ArrayList<String> emails = waitingListDAO.getWaitingList(item.getCode(), storagePlace);
        for (String email : emails) {
            MailSender.sendNotifyWaitingListMail(email, item.getCode(), item.getTitle(), storagePlace);
        }
        waitingListDAO.removeWaitingList(item.getCode(), storagePlace);
        return true;
    }


    public void reserveItem(Hirer hirer, Item item, String storagePlace, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Hirer"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Hirer.");
        }
        if(hirer.getUnbannedDate() != null){
            throw new ActionDeniedException("Errore: l'Hirer con userCode [" + hirer.getUserCode() +"] è bannato non può eseguire un prestito.");
        }
        Library.valueOf(storagePlace);
        ReservationDAO reservationDAO = new ReservationDAO();

        reservationDAO.addReservation(hirer.getUserCode(), item.getCode(), storagePlace);
        MailSender.sendReservationSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(), storagePlace, LocalDate.now().plusDays(7));
    }

    public void confirmReservationWithdraw(Hirer hirer, Item item, String storagePlace, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        if(!token.getTokenWorkingPlace().equals(storagePlace)){
            throw new ActionDeniedException("Errore: Non puoi registrare questa operazione, il libro non è stato prenotato nella sede in cui lavori.");
        }
        Library.valueOf(storagePlace);

        ConnectionManager.closeAutoCommit();
        ReservationDAO reservationDAO = new ReservationDAO();
        LendingController lendingController = new LendingController();
        try {
            reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), storagePlace);
        } catch (Exception e){
            ConnectionManager.rollback();
            throw e;
        }

        try {
            lendingController.registerLending(hirer, item, token);
            ConnectionManager.commit();
            MailSender.sendWithdrawSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(),
                    storagePlace, LocalDate.now().plusMonths(1).toString());
        }catch (Exception e){
            ConnectionManager.rollback();
            throw e;
        }
    }
}
