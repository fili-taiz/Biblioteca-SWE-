package com.progetto_swe.business_logic;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.HirerDAO;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.ReservationDAO;
import com.progetto_swe.orm.WaitingListDAO;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class LendingController {
    public ArrayList<Lending> getLendings(String userCode) {
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendingsByUserCode(userCode);
    }


    public void registerReturnOfItem(Hirer hirer, Item item, Library storagePlace, Token token) {
        Objects.requireNonNull(hirer);//TODO aggiungi requireNonNull
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            //TODO lancia eccezione
        }
        if (!token.getTokenWorkingPlace().equals(storagePlace.toString())) {
            //TODO lancia eccezione
        }
        if (hirer == null) {
            //return false;
        }
        if (item == null) {
            //return false;
        }//TODO eccezioni
        //if(!lendingDAO.getLendings_().haveLending(lending)){//gestisco lanciando eccezione
        //    return false;
        //}

        LendingDAO lendingDAO = new LendingDAO();

        /*cancella lending */
        try {
            lendingDAO.removeLending(hirer.getUserCode(), item.getCode(), storagePlace.toString());
            WaitingListDAO waitingListDAO = new WaitingListDAO();
            ArrayList<String> emails = waitingListDAO.getWaitingList(item.getCode(), storagePlace.toString());
            for (String email : emails) {
                MailSender.sendReturnSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle());//notifica libro disponibile per prenotazione e noleggio
            }
        } catch (Exception e) {
        }//TODO

    }

    public void registerLending(Hirer hirer, Item item, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            //TODO lancia eccezione
        }
        if (!token.getTokenWorkingPlace().equals(token.getTokenWorkingPlace())) {
            //TODO lancia eccezione
        }
        if (hirer.getUnbannedDate() != null) {
            //return false;
        }
        if (!item.getLibraryPhysicalCopies(Library.valueOf(token.getTokenWorkingPlace())).isBorrowable()) {
            //return false;
        }
        if (item.getLibraryPhysicalCopies(Library.valueOf(token.getTokenWorkingPlace())).getNumberOfAvailableCopies() <= 0) {
            //return false;
        }

        ReservationDAO reservationDAO = new ReservationDAO();
        LendingDAO lendingDAO = new LendingDAO();

        //if(lendingDAO.getLendings_().lendingExist(hirer, item, this.admin.getWorkingPlace())){
        //    return false;
        //} controllo da eccezione

        try {
            lendingDAO.addLending(hirer.getUserCode(), item.getCode(), token.getTokenWorkingPlace());
            MailSender.sendLendingSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(), token.getTokenWorkingPlace(), LocalDate.now().plusMonths(1));

        } catch (Exception e) {

        }
    }

}
