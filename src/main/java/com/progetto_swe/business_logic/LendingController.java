package com.progetto_swe.business_logic;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.LendingDAO;
import com.progetto_swe.orm.WaitingListDAO;


import java.time.LocalDate;
import java.util.ArrayList;

public class LendingController {
    public ArrayList<Lending> getLendings(String userCode) {
        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.getLendingsByUserCode(userCode);
    }


    public void registerReturnOfItem(Hirer hirer, Item item, String storagePlace, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        if (!token.getTokenWorkingPlace().equals(storagePlace)) {
            throw new ActionDeniedException("Errore: Non puoi registrare questa operazione, il libro non è stato noleggiato nella sede in cui lavori.");
        }
        Library.valueOf(storagePlace);

        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.removeLending(hirer.getUserCode(), item.getCode(), token.getTokenWorkingPlace());
        MailSender.sendReturnSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle());
        WaitingListDAO waitingListDAO = new WaitingListDAO();
        ArrayList<String> emails = waitingListDAO.getWaitingList(item.getCode(), storagePlace);
        for (String email : emails) {
            MailSender.sendNotifyWaitingListMail(email, item.getCode(), item.getTitle(), storagePlace);//notifica libro disponibile per prenotazione e noleggio
        }
    }

    public void registerLending(Hirer hirer, Item item, Token token) {
        if (!token.getTokenRole().equals(Hasher.hash("Admin"))) {
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        if (hirer.getUnbannedDate() != null) {
            throw new ActionDeniedException("Errore: l'Hirer con userCode [" + hirer.getUserCode() +"] è bannato non può eseguire un prestito.");
        }
        if (!item.isBorrowable(Library.valueOf(token.getTokenWorkingPlace()))) {
            throw new ActionDeniedException("Errore: l'articolo con itemCode [" + item.getCode() +"] non è noleggiabile.");
        }
        if (item.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) <= 0) {
            throw new ActionDeniedException("Errore: l'articolo con itemCode [" + item.getCode() +"] non ha abbastanza copie nella sede [" + token.getTokenWorkingPlace() + "].");
        }

        LendingDAO lendingDAO = new LendingDAO();
        lendingDAO.addLending(hirer.getUserCode(), item.getCode(), token.getTokenWorkingPlace());
        MailSender.sendLendingSuccessMail(hirer.getEmail(), hirer.getUserCode(), item.getCode(), item.getTitle(), token.getTokenWorkingPlace(), LocalDate.now().plusMonths(1));
    }

}
