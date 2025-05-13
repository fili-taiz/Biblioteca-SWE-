package com.progetto_swe.business_logic;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemController {
  /*  public ArrayList<Item> searchItem(String keywords, Category category) {
        ArrayList<Item> items = getAllItems();

        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(category) && i.contains(keyword)) {
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


    public ArrayList<Item> advanceSearchItem(String keywords, Category category, Language language, boolean borrowable, LocalDate startDate, LocalDate endDate) {
        ArrayList<Item> items = getAllItems();
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : items) {
            for (String keyword : splittedKeyword){
                if (i.getCategory().equals(category) && i.getLanguage().equals(language) && i.isBorrowable() == borrowable &&
                        i.getPublicationDate().isAfter(startDate) && i.getPublicationDate().isBefore(endDate) && i.contains(keyword)) {
                    result.add(i);
                }
            }
        }
        return result;
    }


    public void addBook(String title,
                        String publicationDate,
                        String language,
                        String category,
                        String link,
                        String isbn,
                        String publishingHouse,
                        int numberOfPages,
                        String authors,
                        int numberOfCopies,
                        boolean borrowable,
                        Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        Book bookCopy = new Book(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, isbn, publishingHouse, numberOfPages, authors);
        BookDAO bookDAO = new BookDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(bookCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                return; //return true;
            }
        }

        try{
            int itemCode = bookDAO.addBook(
                    title,
                    publicationDate,
                    language,
                    category,
                    link,
                    isbn,
                    publishingHouse,
                    numberOfPages,
                    authors,
                    token.getTokenWorkingPlace(),
                    numberOfCopies,
                    borrowable);
            //return itemCode; //TODO controllare se serve ritornare il codice
        } catch (Exception e) {//RODO gestione
            e.getMessage();
            e.printStackTrace();
        }
        //return false;
    }//TODO gestisci anche quelli dopo

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
            //TODO lancia eccezione
        }

        Magazine magazineCopy = new Magazine(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, numberOfPages, publishingHouse);
        MagazineDAO magazineDAO = new MagazineDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(magazineCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                //return true;
            }
        }

        try{
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
            //return true;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        //return false;
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
            //TODO lancia eccezione
        }

        Thesis thesisCopy = new Thesis(title, LocalDate.parse(publicationDate), Language.valueOf(language), Category.valueOf(category), link, numberOfPages, author, supervisors, university);
        ThesisDAO thesisDAO = new ThesisDAO();

        ArrayList<Item> items = getAllItems();
        for (Item i : items) {
            if(i.sameField(thesisCopy)) {
                addPhysicalCopies(i.getCode(), Library.valueOf(token.getTokenWorkingPlace()), numberOfCopies, borrowable);
                //return true;
            }
        }

        try{
            ConnectionManager.closeAutoCommit();
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
            ConnectionManager.commit();
            //return true;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        //return false;
    }

    public void addPhysicalCopies(int itemCode, Library storagePlace, int numberOfCopies, boolean borrowable) {
        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        int physicalCopies = physicalCopiesDAO.getPhysicalCopies(itemCode).get(storagePlace).getNumberOfPhysicalCopies();
        if (physicalCopies == 0){
            physicalCopiesDAO.addPhysicalCopies(itemCode, storagePlace.toString(), numberOfCopies, borrowable);
        }else {
            physicalCopiesDAO.updatePhysicalCopies(itemCode, storagePlace.toString(), (physicalCopies + numberOfCopies), borrowable);
        }//TODO controlla eccezioni
    }



    public void removeBook(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        if(!physicalCopiesDAO.getPhysicalCopies(itemCode).isEmpty()){
            physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            //return true;
        }

        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBook(itemCode);
        try {
            if(book.getLink().isEmpty()){
                bookDAO.removeBook(itemCode);
            }
        } catch (Exception e) {
            //throw eccezione
        }
        //return true;
    }


    public void removeMagazine(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        if(!physicalCopiesDAO.getPhysicalCopies(itemCode).isEmpty()){
            physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            //return true;
        }

        MagazineDAO magazineDAO = new MagazineDAO();
        Magazine magazine = magazineDAO.getMagazine(itemCode);
        try {
            if(magazine.getLink().isEmpty()){
                magazineDAO.removeMagazine(itemCode);
            }
        } catch (Exception e) {
            //throw eccezione
        }
        //return true;
    }


    public void removeThesis(int itemCode, Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        PhysicalCopiesDAO physicalCopiesDAO = new PhysicalCopiesDAO();
        if(!physicalCopiesDAO.getPhysicalCopies(itemCode).isEmpty()){
            physicalCopiesDAO.removePhysicalCopies(itemCode, token.getTokenWorkingPlace());
            //return true;
        }

        ThesisDAO thesisDAO = new ThesisDAO();
        Thesis thesis = thesisDAO.getThesis(itemCode);
        try {
            if(thesis.getLink().isEmpty()){
                thesisDAO.removeThesis(itemCode);
            }
        } catch (Exception e) {
            //throw eccezione
        }
        //return true;
    }



    public void updateBook(int originalItemCode,
                              String title,
                              String publicationDate,
                              boolean borrowable,
                              String language,
                              String category,
                              String link,
                              String isbn,
                              String publishingHouse,
                              int numberOfPages,
                              String authors,
                              int numberOfCopies,
                              Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        //if(catalogue.getItem(originalItemCode) == null){
        //    return false;
        //} TODO update lancia eccezione se non esistente

        BookDAO bookDAO = new BookDAO();

        try{
            bookDAO.updateBook(originalItemCode, title, publicationDate, language, category, link, isbn, publishingHouse, authors, token.getTokenWorkingPlace(), numberOfCopies, borrowable);

        }catch (Exception e){
            //TODO lancia eccezione
        }
        //return true;
    }

    public void updateMagazine(int originalItemCode,
                                  String title,
                                  String publicationDate,
                                  boolean borrowable,
                                  String language,
                                  String category,
                                  String link,
                                  String isbn,
                                  String publishingHouse,
                                  int numberOfCopies,
                                  Token token) {

        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        //if(catalogue.getItem(originalItemCode) == null){
        //    return false;
        //} TODO update lancia eccezione se non esistente

        MagazineDAO magazineDAO = new MagazineDAO();
        try{
            magazineDAO.updateMagazine(originalItemCode, title, publicationDate, language, category, link, publishingHouse, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
        } catch (Exception e) {
            ConnectionManager.rollback();
            //TODO lancia eccezione
        }
        //return true;
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
                                Token token) {
        if(!token.getTokenRole().equals(Hasher.hash("Admin"))){
            //TODO lancia eccezione
        }

        //if(catalogue.getItem(originalItemCode) == null){
        //    return false;
        //} TODO update lancia eccezione se non esistente

        ThesisDAO thesisDAO = new ThesisDAO();
        try{
            thesisDAO.updateThesis(originalItemCode, title, publicationDate, language, category, link, author, supervisors, univeristy, token.getTokenWorkingPlace(), numberOfCopies, borrowable);
        } catch (Exception e) {
            //TODO lancia eccezione
        }

        //return true;
    }*/
}
