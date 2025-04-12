package com.progetto_swe.domain_model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Book extends Item {
    private String isbn;
    private String publishingHouse;
    private String authors;

    public Book(int code, String title, LocalDate publicationDate, Language language, Category category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {
        super(code, title, publicationDate, language, category, link, numberOfPages);
        this.isbn = isbn;
        this.publishingHouse = publishingHouse;
        this.authors = authors;
    }

    public Book(String title, LocalDate publicationDate, Language language, Category category, String link, String isbn, String publishingHouse, int numberOfPages, String authors) {
        super(-1, title, publicationDate, language, category, link, numberOfPages);
        this.isbn = isbn;
        this.publishingHouse = publishingHouse;
        this.authors = authors;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getPublishingHouse() {
        return this.publishingHouse;
    }

    public String getAuthors() {
        return this.authors;
    }


    /*
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
    }*/

    @Override
    public boolean sameField(Item itemsCopy) {
        if (!super.sameField(itemsCopy)) {
            return false;
        }
        if(itemsCopy instanceof Book booksCopy) {
            if (!this.isbn.equals(booksCopy.getIsbn())) {
                return false;
            }
            if (!this.publishingHouse.equals(booksCopy.getPublishingHouse())) {
                return false;
            }
            return this.authors.equals(booksCopy.getAuthors());
        } else{
            return false;
        }
    }

    @Override
    public String[] getValues(){
        return new String[]{this.title, this.authors, this.category.name(), this.publicationDate.toString()};
    }

    @Override
    public ArrayList<String[]> toStringValues() {
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Codice: ", Integer.toString(this.code)});
        data.add(new String[]{"Titolo: ", this.title});
        data.add(new String[]{"Autori: ", this.authors});
        data.add(new String[]{"Data pubblicazione: ", this.publicationDate.toString()});
        data.add(new String[]{"Categoria: ", this.category.toString()});
        data.add(new String[]{"Lingua: ", this.language.toString()});
        data.add(new String[]{"Link: ", this.link});
        data.add(new String[]{"ISBN: ", this.isbn});
        data.add(new String[]{"Casa editrice: ", this.publishingHouse});
        data.add(new String[]{"Numero pagine: ", Integer.toString(this.numberOfPages)});
        return data;
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

    //messo perché equals da solo dava warning, e cercando su stack overflow ogni talvolta che overrido equal dovrei
    //farlo anche con hashCode ma ora non ho voglia di capire come funziona di preciso, ho solo capito che viene invocato
    //dalle hashmap e hashset per metterli come chiave di una tupla, se sono uguali metterli nello stesso pair se no metterlo in una posizione diversa;
    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
