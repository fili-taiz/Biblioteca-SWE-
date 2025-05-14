package com.progetto_swe.business_logic;

import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class ItemController {

    public ArrayList<Item> searchItem(String keywords, String category) {
        ArrayList<Item> items = getAllItems();

        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(Category.valueOf(category)) && i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    private ArrayList<Item> getAllItems() {
        BookDAO bookDAO = new BookDAO();
        MagazineDAO magazineDAO = new MagazineDAO();
        ThesisDAO thesisDAO = new ThesisDAO();
        ArrayList<Item> items = new ArrayList<>();
        items.addAll(bookDAO.getAllBooks());
        items.addAll(magazineDAO.getAllMagazines());
        items.addAll(thesisDAO.getAllThesis());
        return items;
    }


    public ArrayList<Item> advanceSearchItem(String keywords, String category, String language, boolean borrowable, String startDate, String endDate) {
        ArrayList<Item> items = getAllItems();
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(Category.valueOf(category)) &&
                        i.getLanguage().equals(Language.valueOf(language)) &&
                        i.isBorrowable() == borrowable &&
                        i.getPublicationDate().isAfter(LocalDate.parse(startDate)) &&
                        i.getPublicationDate().isBefore(LocalDate.parse(endDate)) &&
                        i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    public void addBook(String title, String publicationDate, String language, String category, String link, String isbn, String publishingHouse,
                        int numberOfPages, String authors, int numberOfCopies, boolean borrowable, Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        Book bookCopy = new Book(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link,
                isbn, publishingHouse, numberOfPages, authors);
        BookDAO bookDAO = new BookDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(bookCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        int itemCode = bookDAO.addBook(title, publicationDate, language, category, link, isbn, publishingHouse, numberOfPages,
                    authors, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
    }

    public void addMagazine(String title,
                            String publicationDate,
                            String language,
                            String category,
                            String link,
                            int numberOfPages,
                            String publishingHouse,
                            int numberOfCopies,
                            boolean borrowable,
                            Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        Magazine magazineCopy = new Magazine(
                title,
                LocalDate.parse(publicationDate),
                Language.valueOf(language),
                Category.valueOf(category),
                link,
                numberOfPages,
                publishingHouse);
        MagazineDAO magazineDAO = new MagazineDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(magazineCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        int itemCode = magazineDAO.addMagazine(
                title,
                publicationDate,
                language,
                category,
                link,
                publishingHouse,
                numberOfPages,
                token.getTokenWorkingPlace(),
                numberOfCopies,
                borrowable);
    }

    public void addThesis(String title,
                          String publicationDate,
                          String language,
                          String category,
                          String link,
                          int numberOfPages,
                          String author,
                          String supervisors,
                          String university,
                          int numberOfCopies,
                          boolean borrowable,
                          Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }

        Thesis thesisCopy = new Thesis(
                title,
                LocalDate.parse(publicationDate),
                Language.valueOf(language),
                Category.valueOf(category),
                link,
                numberOfPages,
                author,
                supervisors,
                university);
        ThesisDAO thesisDAO = new ThesisDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(thesisCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return;
            }
        }

        int itemCode = thesisDAO.addThesis(
                title,
                publicationDate,
                language,
                category,
                link,
                numberOfPages,
                author,
                supervisors,
                university,
                token.getTokenWorkingPlace(),
                numberOfCopies,
                borrowable);
    }

    private void addPhysicalCopies(int itemCode, Library storagePlace, int numberOfCopies, boolean borrowable) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int physicalCopies = physicalCopiesDAO.getPhysicalCopies(itemCode).get(storagePlace).getNumberOfPhysicalCopies();
        if (physicalCopies == 0){
            physicalCopiesDAO.addPhysicalCopies(itemCode, storagePlace.toString(), numberOfCopies, borrowable);
        }else {
            physicalCopiesDAO.updatePhysicalCopies(itemCode, storagePlace.toString(), (physicalCopies + numberOfCopies), borrowable);
        }
    }



    public void removeBook(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBook(itemCode);

        if(book.getNumberOfLibraries() == 0){
            if(book.getLink().isEmpty()){
                bookDAO.removeBook(itemCode);
            }
        }
        if(book.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0){
            throw new ActionDeniedException("Errore: questo Book con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
        }
        if(book.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) !=
                book.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))){
            throw new ActionDeniedException("Errore: questo Book con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" +
                    token.getTokenWorkingPlace() + "]");
        }
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
    }


    public void removeMagazine(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        MagazineDAO magazineDAO = new MagazineDAO();
        Magazine magazine = magazineDAO.getMagazine(itemCode);

        if(magazine.getNumberOfLibraries() == 0){
            if(magazine.getLink().isEmpty()){
                magazineDAO.removeMagazine(itemCode);
            }
        }
        if(magazine.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0){
            throw new ActionDeniedException("Errore: questo Magazine con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
        }
        if(magazine.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) != magazine.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))){
            throw new ActionDeniedException("Errore: questo Magazine con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" + token.getTokenWorkingPlace() + "]");
        }
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
    }


    public void removeThesis(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        ThesisDAO thesisDAO = new ThesisDAO();
        Thesis thesis = thesisDAO.getThesis(itemCode);

        if(thesis.getNumberOfLibraries() == 0){
            if(thesis.getLink().isEmpty()){
                thesisDAO.removeThesis(itemCode);
            }
        }
        if(thesis.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) == 0){
            throw new ActionDeniedException("Errore: questo Thesis con itemCode [" + itemCode + "] non è presente nella tua sede [" + token.getTokenWorkingPlace() + "]");
        }
        if(thesis.getNumberOfCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace())) != thesis.getNumberOfAvailableCopiesInLibrary(Library.valueOf(token.getTokenWorkingPlace()))){
            throw new ActionDeniedException("Errore: questo Thesis con itemCode [" + itemCode + "] ha ancora prenotazioni/prestiti nella tua sede [" + token.getTokenWorkingPlace() + "]");
        }
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
    }



    public void updateBook(int originalItemCode, String title, String publicationDate, boolean borrowable, String language, String category,
                              String link, String isbn, String publishingHouse, int numberOfPages, String authors, int numberOfCopies,
                              Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);

        BookDAO bookDAO = new BookDAO();
        bookDAO.updateBook(originalItemCode, title, publicationDate, language, category, link, isbn,
                publishingHouse, authors, token.getTokenWorkingPlace(), numberOfCopies, borrowable, numberOfPages);
    }

    public void updateMagazine(int originalItemCode,
                               String title,
                               String publicationDate,
                               boolean borrowable,
                               String language,
                               String category,
                               String link,
                               String publishingHouse,
                               int numberOfCopies,
                               Token token,
                               int numberOfPages) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);

        MagazineDAO magazineDAO = new MagazineDAO();

        magazineDAO.updateMagazine(
                originalItemCode,
                title,
                publicationDate,
                language,
                category,
                link,
                publishingHouse,
                token.getTokenWorkingPlace(),
                numberOfCopies,
                borrowable,
                numberOfPages);
    }

    public void updateThesis(int originalItemCode,
                             String title,
                             String publicationDate,
                             boolean borrowable,
                             String language,
                             String category,
                             String link,
                             String author,
                             String supervisors,
                             String univeristy,
                             int numberOfCopies,
                             Token token,
                             int numberOfPages) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            throw new ActionDeniedException("Errore: Questa operazione è eseguibile solo da un Admin.");
        }
        LocalDate.parse(publicationDate);
        Language.valueOf(language);
        Category.valueOf(category);

        ThesisDAO thesisDAO = new ThesisDAO();
        thesisDAO.updateThesis(
                originalItemCode,
                title,
                publicationDate,
                language,
                category,
                link,
                author,
                supervisors,
                univeristy,
                token.getTokenWorkingPlace(),
                numberOfCopies,
                borrowable,
                numberOfPages);
    }
}
