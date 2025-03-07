package domain_model;

import java.time.LocalDate;

public class Book extends Item{
    String isbn;
    String publishing_house_book;
    int number_of_pages;
    String authors;


    public Book(String code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean isBorrowable, String isbn, String publishing_house_book, int number_of_pages, String authors){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.isbn = isbn;
        this.publishing_house_book = publishing_house_book;
        this.number_of_pages = number_of_pages;
        this.authors = authors;
    }

    public Book(){}



    public String getIsbn(){ return this.isbn; }
    public String getPublishingHouseBook(){ return this.publishing_house_book; }
    public int getNumberOfPages(){ return number_of_pages; }
    public String getAuthors(){ return authors; }

    public void setIsbn(String newIsbn){ this.isbn = newIsbn; }
    public void setPublishingHouse(String new_publishing_house){ this.publishing_house_book = new_publishing_house;}
    public void setNumberOfPages(int new_number_of_pages){ this.number_of_pages = new_number_of_pages; }
    public void setAuthors(String newAuthors){ this.authors = newAuthors; }


    @Override
    public boolean updateItem(Item newItem){
        if (newItem instanceof Book){
            super.updateItem(newItem);
            this.setIsbn(((Book) newItem).getIsbn());
            this.setPublishingHouse(((Book) newItem).getPublishingHouseBook());
            this.setNumberOfPages(((Book) newItem).getNumberOfPages());
            this.setAuthors(((Book) newItem).getAuthors());
            return true;
        }
        return false;
    }


}
