package com.progetto_swe.business_logic;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

public class AdminController {

    Admin admin;
    private final Connection connection;

    public AdminController(Admin admin, Connection connection) {
        this.admin = admin;
        this.connection = connection;
    }

    //la password non è inserita dall'utente è il codice di verifica dell'email ottenuto in fase di registrazione
    public boolean registerExternalHirer(String password, String name, String surname, String eMail, String telephoneNumber) {
        HirerDAO hirerDAO = new HirerDAO(connection);
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
        BookDAO bookDAO = new BookDAO(connection);

        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = catalogue.contains(bookCopy);
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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO(connection);

        Item item = catalogue.getItem(code);
        int physicalCopies = item.getLibraryPhysicalCopies(this.admin.getWorkingPlace()).getNumberOfPhysicalCopies();
        if (physicalCopies == 0){
            physicalCopiesDAO.addPhysicalCopies(code, this.admin.getWorkingPlace().name(), numberOfCopies, borrowable);
        }else {
            physicalCopiesDAO.updatePhysicalCopies(code, this.admin.getWorkingPlace().name(), (physicalCopies + numberOfCopies), borrowable);
        }
        return true;
    }

    public boolean addMagazine(String title, String publicationDate, String language, String category, String link, int numberOfPages, String publishingHouse,
                               int numberOfCopies, boolean borrowable) {
        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category)) {
            return false;
        }

        Magazine magazineCopy = new Magazine(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, numberOfPages, publishingHouse);
        MagazineDAO magazineDAO = new MagazineDAO(connection);

        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = catalogue.contains(magazineCopy);
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

    public boolean addThesis(String title, String publicationDate, String language, String category, String link, int numberOfPages, String author,
                             String supervisors, String university, int numberOfCopies, boolean borrowable) {

        //spostare nella cli
        //convalida data
        if (!checkParametersValidity(publicationDate, language, category)) {
            return false;
        }

        Thesis thesisCopy = new Thesis(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, numberOfPages, author, supervisors, university);
        ThesisDAO thesisDAO = new ThesisDAO(connection);

        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();

        try{
            ConnectionManager.closeAutoCommit();
            int code = catalogue.contains(thesisCopy);
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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        if(catalogue.getItem(code).getNumberOfAvailableCopiesInLibrary(lendingDAO.getLendings(), reservationDAO.getReservations(), this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(catalogue.getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        BookDAO bookDAO = new BookDAO(connection);
        ConnectionManager.closeAutoCommit();
        if(!bookDAO.removeBook(code)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    private void removePhysicalCopies(int code) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO(connection);
        physicalCopiesDAO.removePhysicalCopies(code, this.admin.getWorkingPlace().name());
    }


    public boolean removeThesis(int code) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();

        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        if(catalogue.getItem(code).getNumberOfAvailableCopiesInLibrary(lendingDAO.getLendings(), reservationDAO.getReservations(), this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(catalogue.getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        ThesisDAO thesisDAO = new ThesisDAO(connection);
        ConnectionManager.closeAutoCommit();
        if(!thesisDAO.removeThesis(code)){
            ConnectionManager.rollback();
            return false;
        }
        ConnectionManager.commit();
        return true;
    }

    public boolean removeMagazine(int code) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        if(catalogue.getItem(code).getNumberOfAvailableCopiesInLibrary(lendingDAO.getLendings(), reservationDAO.getReservations(), this.admin.getWorkingPlace()) == -1){
            return false;
        }
        removePhysicalCopies(code);

        if(catalogue.getItem(code).getNumberOfLibraries() > 1){
            return true;
        }

        MagazineDAO magazineDAO = new MagazineDAO(connection);
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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        if(catalogue.getItem(originalItemCode) == null){
            return false;
        }

        BookDAO bookDAO = new BookDAO(connection);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO(connection);

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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        if(catalogue.getItem(originalItemCode) == null){
            return false;
        }

        MagazineDAO magazineDAO = new MagazineDAO(connection);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO(connection);

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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        if(catalogue.getItem(originalItemCode) == null){
            return false;
        }

        ThesisDAO thesisDAO = new ThesisDAO(connection);
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO(connection);

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


    public boolean registerLending(Hirer hirer, Item item) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        HirerDAO hirerDAO = new HirerDAO(connection);
        ListOfHirers hirers = hirerDAO.getHirers();
        ReservationDAO reservationDAO = new ReservationDAO();
        LendingDAO lendingDAO = new LendingDAO();
        if(hirers.getHirer(hirer.getUserCode()) == null){
            return false;
        }
        if(hirers.getHirer(hirer.getUserCode()).getUnbannedDate() != null){
            return false;
        }
        if (!catalogue.getItem(item.getCode()).isBorrowable(this.admin.getWorkingPlace())) {
            return false;
        }
        if(catalogue.getItem(item.getCode()).getNumberOfAvailableCopiesInLibrary(lendingDAO.getLendings(), reservationDAO.getReservations(), this.admin.getWorkingPlace()) <= 1){
            return false;
        }
        if(lendingDAO.getLendings().lendingExist(hirer, item, this.admin.getWorkingPlace())){
            return false;
        }

        return lendingDAO.addLending(hirer.getUserCode(), item.getCode(), this.admin.getWorkingPlace().name());
    }

    public boolean confirmReservationWithdraw(Reservation reservation) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        HirerDAO hirerDAO = new HirerDAO(connection);
        ListOfHirers hirers = hirerDAO.getHirers();
        ReservationDAO reservationDAO = new ReservationDAO();
        LendingDAO lendingDAO = new LendingDAO();

        Hirer hirer = hirers.getHirer(reservation.getHirer().getUserCode());
        Item item = catalogue.getItem(reservation.getItem().getCode());

        if(hirer == null){
            return false;
        }

        if(item == null){
            return false;
        }

        if(reservationDAO.getReservations().haveReservation(reservation)){
            return false;
        }

        /*cancella reservation */
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
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        HirerDAO hirerDAO = new HirerDAO(connection);
        LendingDAO lendingDAO = new LendingDAO();

        Catalogue catalogue = catalogueDAO.getCatalogue();
        ListOfHirers hirers = hirerDAO.getHirers();
        Hirer hirer = hirers.getHirer(lending.getHirer().getUserCode());
        Item item = catalogue.getItem(lending.getItem().getCode());


        if(hirer == null){
            return false;
        }
        if(item == null){
            return false;
        }

        if(!lendingDAO.getLendings().haveLending(lending)){
            return false;
        }

        /*cancella reservation */
        if(lendingDAO.removeLending(lending.getHirer().getUserCode(), lending.getItem().getCode(), lending.getStoragePlace().name())) {
            WaitingListDAO waitingListDAO = new WaitingListDAO(connection);
            ArrayList<String> emails = waitingListDAO.getWaitingList(lending.getItem().getCode(), lending.getStoragePlace().toString());
            MailSender mailSender = new MailSender();
            for (String email : emails) {
                mailSender.mandaMail();//notifica libro disponibile per prenotazione e noleggio
            }
            return true;
        }
        return false;
    }

    public ArrayList<Item> searchItem(String keyWords, String category) {
        CatalogueDAO catalogueDAO = new CatalogueDAO(connection);
        Catalogue catalogue = catalogueDAO.getCatalogue();
        return admin.searchItem(catalogue, keyWords, Category.valueOf(category));
    }

    public ArrayList<Hirer> searchHirer(String keyWords) {
        HirerDAO hirerDAO = new HirerDAO(connection);
        ListOfHirers hirers = hirerDAO.getHirers();
        return hirers.searchHirer(keyWords);
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
