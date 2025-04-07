package com.progetto_swe.business_logic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.progetto_swe.domain_model.Admin;
import com.progetto_swe.domain_model.Book;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Hirer;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Lending;
import com.progetto_swe.domain_model.Magazine;
import com.progetto_swe.domain_model.Reservation;
import com.progetto_swe.domain_model.Thesis;
import com.progetto_swe.orm.*;

public class AdminController {
/*
    Admin admin;

    public AdminController(Admin admin) {
        this.admin = admin;
    }

    //la password non è inserita dall'utente è il codice di verifica dell'email ottenuto in fase di registrazione
    public boolean registerExternalHirer(String password, String name, String surname, String eMail, String telephoneNumber) {
        HirerDAO hirerDAO = new HirerDAO();
        String userCode = "";
        do { //generazione codice univoco per chiave primaria con prefisso E per non occupare future possibili matricole
            userCode = "E" + Math.round((Math.random() * 1000000));
        } while (hirerDAO.getHirer(userCode) != null);
        String salt = String.valueOf(Math.round(Math.random()*100000));
        String hashedPassword = Hasher.hash(password, salt);

        //controllo non sia gia' presente
        if(hirerDAO.getHirer(userCode) != null) {
            return false;
        }

        //avvio transazione per prevenire problemi causati dal successo della sola prima operazione
        ConnectionManager.closeAutoCommit();
        if(!hirerDAO.addHirer(userCode, name, surname, eMail, telephoneNumber)){
            ConnectionManager.rollback();
            return false;
        }
        if(!hirerDAO.addHirerPassword(userCode, salt, hashedPassword)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        ConnectionManager.openAutoCommit();

        return true;
    }

    public boolean addBook(String title, String publicationDate, String language, String category, String link, String isbn,
                           String publishingHouse, int numberOfPages, String authors, int numberOfCopies, boolean borrowable) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category)) {
            return false;
        }

        Book bookCopy = new Book(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, isbn, publishingHouse, numberOfPages, authors);
        BookDAO bookDAO = new BookDAO();

        refreshCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = this.admin.getCatalogue().contains(bookCopy);
            if(code ==  -1){
                code = bookDAO.addBook(title, publicationDate, language, category, link, isbn, publishingHouse, numberOfPages, authors);

                if(code == -1){
                    ConnectionManager.rollback();
                    return false;
                }
            }

            if(!addPhysicalCopies(code, numberOfCopies, borrowable)){
                ConnectionManager.rollback();
                return false;
            }
            ConnectionManager.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private boolean addPhysicalCopies(int code, int numberOfCopies, boolean borrowable) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int physicalCopies = this.admin.getCatalogue().getItem(code).getNumberOfAvailableCopiesInLibrary(this.admin.getWorkingPlace());
        if (physicalCopies == 0){
            physicalCopiesDAO.addPhysicalCopies(code, this.admin.getWorkingPlace().name(), numberOfCopies, borrowable);
        }else {
            physicalCopiesDAO.updatePhysicalCopies(code, this.admin.getWorkingPlace().name(), (physicalCopies + numberOfCopies), borrowable);
        }
        return true;
    }

    public boolean addMagazine(String title, String publicationDate, String language, String category, String link, String publishingHouse,
                               int numberOfCopies, boolean borrowable) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category)) {
            return false;
        }

        Magazine magazineCopy = new Magazine(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, publishingHouse);
        MagazineDAO magazineDAO = new MagazineDAO();

        refreshCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = this.admin.getCatalogue().contains(magazineCopy);
            if(code ==  -1){
                code = magazineDAO.addMagazine(title, publicationDate, language, category, link, publishingHouse);

                if(code == -1){
                    ConnectionManager.rollback();
                    return false;
                }
            }

            if(!addPhysicalCopies(code, numberOfCopies, borrowable)){
                ConnectionManager.rollback();
                return false;
            }
            ConnectionManager.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean addThesis(String title, String publicationDate, String language, String category, String link, String author,
                             String supervisors, String university, int numberOfCopies, boolean borrowable) {

        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category)) {
            return false;
        }

        Thesis thesisCopy = new Thesis(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, author, supervisors, university);
        ThesisDAO thesisDAO = new ThesisDAO();

        refreshCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = this.admin.getCatalogue().contains(thesisCopy);
            if(code ==  -1){
                code = thesisDAO.addThesis(title, publicationDate, language, category, link, author, supervisors, university);

                if(code == -1){
                    ConnectionManager.rollback();
                    return false;
                }
            }

            if(!addPhysicalCopies(code, numberOfCopies, borrowable)){
                ConnectionManager.rollback();
                return false;
            }
            ConnectionManager.commit();
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean removeBook(int code) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(code).getNumberOfAvailableCopiesInLibrary(this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(this.admin.getCatalogue().getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        BookDAO bookDAO = new BookDAO();
        ConnectionManager.closeAutoCommit();
        if(!bookDAO.removeBook(code)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    private void removePhysicalCopies(int code) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.removePhysicalCopies(code, this.admin.getWorkingPlace().name());
    }


    public boolean removeThesis(int code) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(code).getNumberOfAvailableCopiesInLibrary(this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(this.admin.getCatalogue().getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        ThesisDAO thesisDAO = new ThesisDAO();
        ConnectionManager.closeAutoCommit();
        if(!thesisDAO.removeThesis(code)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean removeMagazine(int code) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(code).getNumberOfAvailableCopiesInLibrary(this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(this.admin.getCatalogue().getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        MagazineDAO magazineDAO = new MagazineDAO();
        ConnectionManager.closeAutoCommit();
        if(!magazineDAO.removeMagazine(code)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean updateBook(int originalItemCode, String title, String publicationDate, boolean borrowable, String language, String category, String link,
                              String isbn, String publishingHouse, int numberOfPages, String authors, int numberOfCopies) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(originalItemCode) == null){
            return false;
        }

        BookDAO bookDAO = new BookDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();

        ConnectionManager.closeAutoCommit();
        if(!bookDAO.updateBook(originalItemCode, title, publicationDate, language, category, link, isbn, publishingHouse, numberOfPages,
                authors)){
            ConnectionManager.rollback();
            return false;
        }
        if(!physicalCopiesDAO.updatePhysicalCopies(originalItemCode,this.admin.getWorkingPlace().name(), numberOfCopies, borrowable)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean updateMagazine(int originalItemCode, String title, String publicationDate, boolean borrowable, String language, String category, String link,
                              String isbn, String publishingHouse, int numberOfCopies) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(originalItemCode) == null){
            return false;
        }

        MagazineDAO magazineDAO = new MagazineDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();

        ConnectionManager.closeAutoCommit();
        if(!magazineDAO.updateMagazine(originalItemCode, title, publicationDate, language, category, link, publishingHouse)){
            ConnectionManager.rollback();
            return false;
        }
        if(!physicalCopiesDAO.updatePhysicalCopies(originalItemCode,this.admin.getWorkingPlace().name(), numberOfCopies, borrowable)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean updateThesis(int originalItemCode, String title, String publicationDate, boolean borrowable, String language, String category, String link,
                              String author, String supervisors, String univeristy, int numberOfCopies) {
        refreshCatalogue();
        if(this.admin.getCatalogue().getItem(originalItemCode) == null){
            return false;
        }

        ThesisDAO thesisDAO = new ThesisDAO();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();

        ConnectionManager.closeAutoCommit();
        if(!thesisDAO.updateThesis(originalItemCode, title, publicationDate, language, category, link, author, supervisors, univeristy)){
            ConnectionManager.rollback();
            return false;
        }
        if(!physicalCopiesDAO.updatePhysicalCopies(originalItemCode,this.admin.getWorkingPlace().name(), numberOfCopies, borrowable)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }


    public boolean registerLending(String userCode, int itemCode) {
        refreshCatalogue();
        refreshHirers();
        if(this.admin.getListOfHirers().getHirer(userCode) == null){
            return false;
        }

        if(this.admin.getListOfHirers().getHirer(userCode).getUnbannedDate() != null){
            return false;
        }

        if (!this.admin.getCatalogue().getItem(itemCode).isBorrowable(this.admin.getWorkingPlace())) {
            return false;
        }

        if(this.admin.getCatalogue().getItem(itemCode).getNumberOfAvailableCopiesInLibrary(this.admin.getWorkingPlace()) <= 1){
            return false;
        }



        LendingDAO lendingDAO = new LendingDAO();
        return lendingDAO.addLending(userCode, itemCode, this.admin.getWorkingPlace().name());
    }

    public boolean confirmReservationWithdraw(Reservation reservation) {
        refreshCatalogue();
        refreshHirers();
        Hirer hirer = this.admin.getListOfHirers().getHirer(reservation.getHirer().getUserCode());
        Item item = this.admin.getCatalogue().getItem(reservation.getItem().getCode());

        if(hirer == null){
            return false;
        }

        if(item != null){
            return false;
        }

        if(hirer.haveReservation(reservation)){
            return false;
        }

        if (item.haveReservation(reservation)){
            return false;
        }

        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
*/
    /*
        cancella reservation
        ConnectionManager.closeAutoCommit();
        if (!reservationDAO.removeReservation(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name())) {
            ConnectionManager.rollback();
            return false;
        }
        if(!lendingDAO.addLending(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name())){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean registerItemReturn(Lending lending) {
        refreshCatalogue();
        refreshHirers();
        Hirer hirer = this.admin.getListOfHirers().getHirer(lending.getHirer().getUserCode());
        Item item = this.admin.getCatalogue().getItem(lending.getItem().getCode());

        if(hirer == null){
            return false;
        }
        if(item == null){
            return false;
        }

        if(hirer.haveLending(lending)){
            return false;
        }

        if (item.haveLending(lending)){
            return false;
        }

        LendingDAO lendingDAO = new LendingDAO();
        *//*cancella reservation */
        /*return lendingDAO.removeLending(lending.getHirer().getUserCode(), lending.getItem().getCode(), lending.getStoragePlace().name());
    }

    public ArrayList<Item> searchItem(String keyWords, String category) {
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.refreshCatalogue();
        return admin.searchItem(keyWords, Category.valueOf(category));
    }

    public ArrayList<Hirer> searchHirer(String keyWords) {
        refreshHirers();
        return admin.searchHirer(keyWords);
    }

    private void refreshCatalogue() {
        AdminDAO adminDAO = new AdminDAO();
        this.admin.setCatalogue(adminDAO.refreshCatalogue());
    }

    private void refreshHirers() {
        AdminDAO adminDAO = new AdminDAO();
        this.admin.setHirers(adminDAO.refreshHirers());
    }

    //controllo validità parametri;
    //da spostare nella cli
    private boolean checkParametersValidity(String publicationDate, String language, String category) {
        //convalida data
        try {
            LocalDate.parse(publicationDate);
        } catch (DateTimeParseException e) {
            return false;
        }


        //convalida lingua
        try {
            Language.valueOf(language);
        } catch (IllegalArgumentException e) {
            return false;
        }

        //convalida categoria
        try {
            Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
*/
}

/**
 * //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) authors.sort(String::compareTo); String concatenateAuthors = "";
 * for (String author : authors) { concatenateAuthors += author + ", "; }
 * concatenateAuthors = concatenateAuthors.substring(0,
 * concatenateAuthors.length() - 1);
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * //spostare nella cli //creazione stringa contenente tutti gli autori (ordine
 * alfabetico) supervisors.sort(String::compareTo); String
 * concatenateSupervisors = ""; for (String supervisor : supervisors) {
 * concatenateSupervisors += supervisor + ", "; } concatenateSupervisors =
 * concatenateSupervisors.substring(0, concatenateSupervisors.length() - 1);
 */
