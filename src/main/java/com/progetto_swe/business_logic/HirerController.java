package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.util.ArrayList;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

public class HirerController {
    private Hirer hirer;

    public HirerController(Hirer hirer){
        this.hirer = hirer;
    }

    public void addInWaitingList(Item item, Library storagePlace){
        if(item.getLibraryPhysicalCopies(storagePlace).isBorrowable()){
            return; //TODO eccezione
        }
        WaitingListDAO waitingListDAO = new WaitingListDAO();
        waitingListDAO.addToWaitingList(item.getCode(), storagePlace.toString(), this.hirer.getEmail());
    }

    public ArrayList<Hirer> searchHirer(String keywords) {
        HirerDAO hirerDAO = new HirerDAO();
        ArrayList<Hirer> hirers = hirerDAO.getHirers_();
        ArrayList<Hirer> result = new ArrayList<>();
        for(Hirer h : hirers){
            if(!h.contains(keywords)){
                result.add(h);
            }
        }
        return result;
    }

    //la password non è inserita dall'utente è il codice di verifica dell'email ottenuto in fase di registrazione
   /* public void registerExternalHirer(String password, String name, String surname, String eMail, String telephoneNumber, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }
        HirerDAO hirerDAO = new HirerDAO();
        String userCode = "";
        do { //generazione codice univoco per chiave primaria con prefisso E per non occupare future possibili matricole
            userCode = "E" + Math.round((Math.random() * 1000000));
        } while (hirerDAO.getHirer(userCode) != null);
        String salt = String.valueOf(Math.round(Math.random()*100000));
        String hashedPassword = Hasher.hashPassword(password, salt);

        //avvio transazione per prevenire problemi causati dal successo della sola prima operazione
        ConnectionManager.closeAutoCommit();

        hirerDAO.addHirer(userCode, name, surname, eMail, telephoneNumber, hashedPassword, salt);

            ConnectionManager.commit();
        } catch (Exception e){
            ConnectionManager.rollback();
            //return false;//TODO throw
        }
    }*/

}
