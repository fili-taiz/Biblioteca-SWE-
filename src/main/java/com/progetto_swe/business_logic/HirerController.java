package com.progetto_swe.business_logic;

import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;
import com.progetto_swe.orm.database_exception.IdNotFoundException;
import com.progetto_swe.university_authentication_system.UniversityAuthenticationSystem;

public class HirerController {
    public Hirer loginUniversityHirer(String userCode, String password){
        UniversityAuthenticationSystem authenticationSystem = new UniversityAuthenticationSystem();

        //ottengo informazioni di questo UniversityHirer se la password combacia con quella nel database universitario
        HashMap<String, String> hirerInfo = authenticationSystem.getUniversityPeople(userCode, password);


        //non riconosciuto dall'università
        if (hirerInfo.isEmpty()) {
            throw new AccessDeniedException("Errore: accesso come ruolo Hirer rifiutato, controlla userCode e password.");
        }

        //ottengo informazioni di questo UniversityHirer nel database bibliotecario
        HirerDAO hirerDAO = new HirerDAO();
        Hirer hirer;
        try{
            hirer = hirerDAO.getHirer(userCode);
        }catch (IdNotFoundException e) { //riconosciuto dall'università ma è la prima volta che esegue login
            hirerDAO.addHirer( userCode, hirerInfo.get("name"), hirerInfo.get("surname"), hirerInfo.get("email"),
                    hirerInfo.get("telephoneNumber"));
            hirer = new Hirer(userCode, hirerInfo.get("name"), hirerInfo.get("surname"), hirerInfo.get("email"),
                    hirerInfo.get("telephoneNumber"),
                    null, null);
        }
        //aggiunta credenziali
        hirer.setToken(new Token(hirer));

        //riconosciuto dal sistema universitario e presente nel database della biblioteca
        return hirer;
    }

    public Hirer loginExternalHirer(String userCode, String password)  throws AccessDeniedException {
        HirerDAO hirerDAO = new HirerDAO();
        HashMap<String, String> saltAndHashedPassword = hirerDAO.getSaltAndHashedPassword(userCode);

        //controllo password
        if(!Hasher.hashPassword(password,saltAndHashedPassword.get("salt")).equals(saltAndHashedPassword.get("hashedPassword"))){
            throw new AccessDeniedException("Errore: accesso come ruolo Hirer rifiutato, controlla userCode e password.");
        }

        //istanziazione Hirer
        Hirer hirer = hirerDAO.getHirer(userCode);
        hirer.setToken(new Token(hirer));
        return hirer;
    }

    public void addToWaitingList(Item item, String mail, String storagePlace){
        if(!item.isBorrowable(Library.valueOf(storagePlace))){
            throw new ActionDeniedException("Errore: L'Item con itemCode [" +  item.getCode() + "] non è noleggiabile nella sede [" + storagePlace + "].");
        }
        WaitingListDAO waitingListDAO = new WaitingListDAO();
        waitingListDAO.addToWaitingList(item.getCode(), storagePlace, mail);
    }

    public ArrayList<Hirer> searchHirer(String keyword) {
        HirerDAO hirerDAO = new HirerDAO();
        ArrayList<Hirer> hirers = hirerDAO.getHirers_();
        ArrayList<Hirer> result = new ArrayList<>();
        for(Hirer h : hirers){
            if(!h.contains(keyword)){
                result.add(h);
            }
        }
        return result;
    }

    public void registerExternalHirer(String name, String surname, String email, String telephoneNumber, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        HirerDAO hirerDAO = new HirerDAO();
        String password = "Password" + Math.round((Math.random() * 1000000));
        String userCode = "";
        try{
            do {
                userCode = "E" + Math.round((Math.random() * 1000000));
                hirerDAO.getHirer(userCode);
            } while (true);
        } catch (IdNotFoundException e) {
        }
        String salt = String.valueOf(Math.round(Math.random()*100000));
        String hashedPassword = Hasher.hashPassword(password, salt);


        ConnectionManager.closeAutoCommit();
        try{
            hirerDAO.addHirer(userCode, name, surname, email, telephoneNumber);
            hirerDAO.addHirerPassword(userCode, hashedPassword, salt);
            ConnectionManager.commit();
        } catch (Exception e){
            ConnectionManager.rollback();
            throw e;
        }
    }

}
