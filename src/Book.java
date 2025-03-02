import java.time.LocalDate;
import java.util.ArrayList;

public class Book extends Item{
    String isbn;
    String publishingHouseBook;
    int numberOfPages;
    String authors;

    public Book(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable, String isbn, String publishingHouseBook, int numberOfPages, String authors){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.isbn = isbn;
        this.publishingHouseBook = publishingHouseBook;
        this.numberOfPages = numberOfPages;
        this.authors = authors;
    }



    public String get_isbn(){ return this.isbn; }
    public String get_publishing_house(){ return this.publishingHouseBook; }
    public int get_number_of_pages(){ return numberOfPages; }
    public String get_authors(){ return authors; }

    public void set_isbn(String newIsbn){ this.isbn = newIsbn; }
    public void set_publishing_house(String new_publishing_house){ this.publishingHouseBook = new_publishing_house;}
    public void set_number_of_pages(int new_number_of_pages){ this.numberOfPages = new_number_of_pages; }
    public void set_authors(String newAuthors){ this.authors = newAuthors; }


    public void update_book(Book newBook){
        update_item(newBook);
        this.isbn = newBook.isbn;
        this.publishingHouseBook = newBook.publishingHouseBook;
        this.numberOfPages = newBook.numberOfPages;
        this.authors = newBook.authors;
    }


}
