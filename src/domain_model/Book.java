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
    public int getNumberOfPages(){ return this.number_of_pages; }
    public String getAuthors(){ return this.authors; }

    public void setIsbn(String newIsbn){ this.isbn = newIsbn; }
    public void setPublishingHouse(String new_publishing_house){ this.publishing_house_book = new_publishing_house;}
    public void setNumberOfPages(int new_number_of_pages){ this.number_of_pages = new_number_of_pages; }
    public void setAuthors(String newAuthors){ this.authors = newAuthors; }


    @Override
    public boolean updateItem(Item new_item){
        if (new_item instanceof Book){
            super.updateItem(new_item);
            this.setIsbn(((Book) new_item).getIsbn());
            this.setPublishingHouse(((Book) new_item).getPublishingHouseBook());
            this.setNumberOfPages(((Book) new_item).getNumberOfPages());
            this.setAuthors(((Book) new_item).getAuthors());
            return true;
        }
        return false;
    }


}
