package com.progetto_swe.domain_model;

import java.time.LocalDate;

public class Book extends Item {
    String isbn;
    String publishingHouse;
    int numberOfPages;
    String authors;

    public Book(int code, String title, LocalDate publicationDate, Language language, Category category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {
        super(code, title, publicationDate, language, category, link);
        this.isbn = isbn;
        this.publishingHouse = publishingHouse;
        this.numberOfPages = numberOfPages;
        this.authors = authors;
    }

    public Book(String title, LocalDate publicationDate, Language language, Category category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {
        super(-1, title, publicationDate, language, category, link);
        this.isbn = isbn;
        this.publishingHouse = publishingHouse;
        this.numberOfPages = numberOfPages;
        this.authors = authors;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getPublishingHouse() {
        return this.publishingHouse;
    }

    public int getNumberOfPages() {
        return this.numberOfPages;
    }

    public String getAuthors() {
        return this.authors;
    }

    public void setIsbn(String newIsbn) {
        this.isbn = newIsbn;
    }

    public void setPublishingHouse(String newPublishingHouse) {
        this.publishingHouse = newPublishingHouse;
    }

    public void setNumberOfPages(int newNumberOfPages) {
        this.numberOfPages = newNumberOfPages;
    }

    public void setAuthors(String newAuthors) {
        this.authors = newAuthors;
    }


    @Override
    public boolean updateItem(Item newItem) {
        if (newItem instanceof Book book) {
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
    public boolean sameField(Item itemsCopy) {
        if (!super.sameField(itemsCopy)) {
            return false;
        }
        Book booksCopy = (Book) itemsCopy;
        if (!this.isbn.equals(booksCopy.getIsbn())) {
            return false;
        }
        if (!this.publishingHouse.equals(booksCopy.getPublishingHouse())) {
            return false;
        }
        if (this.numberOfPages != booksCopy.getNumberOfPages()) {
            return false;
        }
        return this.getAuthors().equals(booksCopy.getAuthors());
    }

    @Override
    public boolean contains(String keyword) {
        if (isbn.contains(keyword)) {
            return true;
        }
        if (publishingHouse.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        if (authors.toUpperCase().contains(keyword.toUpperCase())) {
            return true;
        }
        return super.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        Book b = (Book) o;
        return this.isbn.equals(b.getIsbn()) && this.publishingHouse.equals(b.getPublishingHouse()) && this.numberOfPages == b.getNumberOfPages() && this.authors.equals(b.getAuthors());
    }

    //messo perchè equals da solo dava warning, e cercando su stack overflow ogni talvolta che overrido equal dovrei
    //farlo anche con hashCode ma ora non ho voglia di capire come funziona di preciso, ho solo capito che viene invocato
    //dalle hashmap e hashset per metterli come chiave di una tupla, se sono uguali metterli nello stesso pair se no metterlo in una posizione diversa;
    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
