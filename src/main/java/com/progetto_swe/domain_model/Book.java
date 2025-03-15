package com.progetto_swe.domain_model;

import java.time.LocalDate;

public class Book extends Item{
    String isbn;
    String publishingHouse;
    int numberOfPages;
    String authors;

    public Book(int code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable, String isbn, String publishingHouse, int numberOfPages, String authors){
        super(code, title, publicationDate, language, category, link, borrowable);
        this.isbn = isbn;
        this.publishingHouse = publishingHouse;
        this.numberOfPages = numberOfPages;
        this.authors = authors;
    }

    public String getIsbn(){ return this.isbn; }
    public String getPublishingHouse(){ return this.publishingHouse; }
    public int getNumberOfPages(){ return this.numberOfPages; }
    public String getAuthors(){ return this.authors; }

    public void setIsbn(String newIsbn){ this.isbn = newIsbn; }
    public void setPublishingHouse(String newPublishingHouse){ this.publishingHouse = newPublishingHouse;}
    public void setNumberOfPages(int newNumberOfPages){ this.numberOfPages = newNumberOfPages; }
    public void setAuthors(String newAuthors){ this.authors = newAuthors; }


    @Override
    public boolean updateItem(Item newItem){
        if (newItem instanceof Book book){
            super.updateItem(newItem);
            this.setIsbn(book.getIsbn());
            this.setPublishingHouse(book.getPublishingHouse());
            this.setNumberOfPages(book.getNumberOfPages());
            this.setAuthors(book.getAuthors());
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String keyword){
        if(isbn.contains(keyword)){
            return true;
        }
        if(publishingHouse.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        if(authors.toUpperCase().contains(keyword.toUpperCase())){
            return true;
        }
        return super.contains(keyword);
    }


}
